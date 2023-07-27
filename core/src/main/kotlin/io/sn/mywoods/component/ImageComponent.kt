package io.sn.mywoods.component

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

class ImageComponent : Component<ImageComponent>, Comparable<ImageComponent> {

    lateinit var image: Image

    override fun type() = ImageComponent

    override fun World.onAdd(entity: Entity) {
        this.inject<Stage>("GameStage").addActor(image)
    }

    override fun World.onRemove(entity: Entity) {
        this.inject<Stage>("GameStage").root.removeActor(image)
    }

    companion object : ComponentType<ImageComponent>()

    override fun compareTo(other: ImageComponent): Int = other.image.y.compareTo(image.y).let {
        if (it != 0) {
            it
        } else {
            other.image.x.compareTo(image.x)
        }
    }

}