package io.sn.mywoods.component

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2

data class Size(val width: Float, val height: Float)

enum class SpawnType {
    DEFAULT,
    FIGHTER, SLIME, CHEST_DOWN
}

data class SpawnCfg(
    var model: AnimationModel,
    var aniType: AnimationType = AnimationType.DOWN,
    var aniPlayMode: PlayMode = PlayMode.LOOP_REVERSED,
    var size: Size = Size(2f, 2f),
    var speed: Float = 1 / 4f
)

class SpawnComponent(
    var type: SpawnType = SpawnType.DEFAULT,
    var location: Vector2 = vec2()
) : Component<SpawnComponent> {
    override fun type() = SpawnComponent

    companion object : ComponentType<SpawnComponent>()
}
