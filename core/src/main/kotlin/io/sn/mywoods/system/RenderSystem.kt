package io.sn.mywoods.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.collection.compareEntity
import io.sn.mywoods.component.ImageComponent

class RenderSystem(
    private val stage: Stage,
) : IteratingSystem(family {
    all(ImageComponent)
}, comparator = compareEntity { e1, e2 -> e1[ImageComponent].compareTo(e2[ImageComponent]) }) {

    override fun onTick() {
        super.onTick()

        with(stage) {
            viewport.apply()
            act(deltaTime)
            draw()
        }
    }

    override fun onTickEntity(entity: Entity) {
        entity[ImageComponent].image.toFront()
    }
}