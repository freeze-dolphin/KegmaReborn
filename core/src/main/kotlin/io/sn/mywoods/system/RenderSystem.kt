package io.sn.mywoods.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.collection.compareEntity
import io.sn.mywoods.component.ImageComponent
import io.sn.mywoods.event.MapChangeEvent
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.tiled.forEachLayer

class RenderSystem(
    private val stage: Stage,
) : EventListener, IteratingSystem(family {
    all(ImageComponent)
}, comparator = compareEntity { e1, e2 -> e1[ImageComponent].compareTo(e2[ImageComponent]) }) {

    private val flayer = mutableListOf<TiledMapTileLayer>()
    private val blayer = mutableListOf<TiledMapTileLayer>()

    private val mapRenderer = OrthogonalTiledMapRenderer(null, 1 / 32f, stage.batch)
    private val orthogonalCamera = stage.camera as OrthographicCamera

    override fun onTick() {
        super.onTick()

        with(stage) {
            viewport.apply()

            AnimatedTiledMapTile.updateAnimationBaseTime()
            mapRenderer.setView(orthogonalCamera)

            if (blayer.isNotEmpty()) {
                stage.batch.use(orthogonalCamera.combined) {
                    blayer.forEach { mapRenderer.renderTileLayer(it) }
                }
            }

            stage.run {
                act(deltaTime)
                draw()
            }

            if (flayer.isNotEmpty()) {
                stage.batch.use(orthogonalCamera.combined) {
                    flayer.forEach { mapRenderer.renderTileLayer(it) }
                }
            }
        }
    }

    override fun onTickEntity(entity: Entity) {
        entity[ImageComponent].image.toFront()
    }

    override fun handle(event: Event): Boolean {
        if (event is MapChangeEvent) {
            mapRenderer.map = event.map
            flayer.clear()
            blayer.clear()
            event.map.forEachLayer<TiledMapTileLayer> { layer ->
                if (layer.name.startsWith("1")) {
                    flayer.add(layer)
                } else if (layer.name.startsWith("0")) {
                    blayer.add(layer)
                }
            }
            return true
        }
        return false
    }

    override fun onDispose() {
        mapRenderer.disposeSafely()
    }
}
