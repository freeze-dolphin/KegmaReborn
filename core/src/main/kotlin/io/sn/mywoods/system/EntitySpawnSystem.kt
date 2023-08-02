package io.sn.mywoods.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import io.sn.mywoods.CaseSupport
import io.sn.mywoods.MysticWoods.Companion.UNIT_SCALE
import io.sn.mywoods.component.*
import io.sn.mywoods.event.MapChangeEvent
import ktx.app.gdxError
import ktx.log.logger
import ktx.math.vec2
import ktx.tiled.*

class EntitySpawnSystem : EventListener, IteratingSystem(World.family {
    all(SpawnComponent)
}) {
    private val cachedCfgs = mutableMapOf<SpawnType, SpawnCfg>()
    private fun spawnCfgs(spawnType: SpawnType) = cachedCfgs.getOrPut(spawnType) {
        when (spawnType) {
            SpawnType.FIGHTER -> SpawnCfg(AnimationModel.FIGHTER)
            SpawnType.SLIME -> SpawnCfg(AnimationModel.SLIME).apply { size = Size(1.5f, 1.5f) }
            SpawnType.CHEST_DOWN -> SpawnCfg(AnimationModel.CHEST_DOWN, AnimationType.NO_ANIMATION)
            else -> gdxError("Default spawn type is not allowed! Have you set the `EntityType`?")
        }
    }

    override fun onTickEntity(entity: Entity) {
        with(entity[SpawnComponent]) {
            val cfg = spawnCfgs(type)

            log.debug { "Spawning entity for ${entity.id}" }

            world.entity {
                it += ImageComponent().apply {
                    image = Image().apply {
                        setPosition(location.x * UNIT_SCALE, location.y * UNIT_SCALE)
                        setSize(cfg.size.width, cfg.size.height)
                        setScaling(Scaling.fit)
                    }
                }

                it += AnimationComponent(cfg.model, 0f, cfg.speed, cfg.aniPlayMode).apply {
                    nextFrame(cfg.model, cfg.aniType)
                }
            }
        }

        entity.remove()
    }

    override fun handle(evt: Event?): Boolean {
        when (evt) {
            is MapChangeEvent -> {
                val etyLayer = evt.map.layer("entities")
                etyLayer.objects.forEach { mobj ->
                    val type = mobj.propertyOrNull<String>("type") ?: gdxError("Map object ${mobj.id} has no property named `type`!")
                    world.entity {
                        //it += SpawnComponent(type, vec2(mobj.x * UNIT_SCALE, mobj.y * UNIT_SCALE))
                        it += SpawnComponent(SpawnType.valueOf(CaseSupport.toSnakecase(type).uppercase()), vec2(mobj.x, mobj.y))
                    }
                }

                return true
            }
        }
        return false
    }

    companion object {
        val log = logger<EntitySpawnSystem>()
    }

}