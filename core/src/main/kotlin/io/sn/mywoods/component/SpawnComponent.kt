package io.sn.mywoods.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2



class SpawnComponent(
    var type: String = "",
    var location: Vector2 = vec2()
) : Component<SpawnComponent> {
    override fun type() = SpawnComponent

    companion object : ComponentType<SpawnComponent>()
}