package io.sn.mywoods

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import io.sn.mywoods.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class MysticWoods : KtxGame<KtxScreen>() {

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        addScreen(GameScreen())
        setScreen<GameScreen>()
    }

}
