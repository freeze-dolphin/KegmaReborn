package io.sn.mywoods.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.log.logger

@Suppress("unused")
enum class AnimationModel {
    FIGHTER, SLIME, CHEST_DOWN;

    val atlasKey = this.toString().lowercase()
}

@Suppress("unused")
enum class AnimationType {
    UP, DOWN, LEFT, RIGHT, NO_ANIMATION;

    val atlasKey = this.toString().lowercase()
}

class AnimationComponent(
    private var model: AnimationModel,
    var stateTime: Float = 0f,
    var frameDuration: Float = 1 / 8f,
    var playMode: Animation.PlayMode = Animation.PlayMode.REVERSED,
) : Component<AnimationComponent> {

    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation: String = NO_ANIMATION

    override fun type() = AnimationComponent

    companion object : ComponentType<AnimationComponent>() {
        const val NO_ANIMATION = ""
        private val log = logger<AnimationComponent>()
    }

    fun nextFrame(model: AnimationModel, type: AnimationType) {
        this.model = model
        nextAnimation = if (type == AnimationType.NO_ANIMATION) {
            "#${model.atlasKey}/down"
        } else {
            "${model.atlasKey}/${type.atlasKey}"
        }
        log.debug { "nextAnimation: $nextAnimation" }
    }

}