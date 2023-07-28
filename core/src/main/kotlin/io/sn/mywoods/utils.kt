package io.sn.mywoods

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.World

fun World.registerAllListener(stage: Stage) {
    this.systems.filter {
        it is EventListener
    }.forEach {
        stage.addListener(it as EventListener)
    }
}

fun Stage.fireEvent(evt: Event) {
    this.root.fire(evt)
}