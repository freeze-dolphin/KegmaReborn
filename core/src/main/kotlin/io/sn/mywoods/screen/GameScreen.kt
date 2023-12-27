package io.sn.mywoods.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.*
import com.github.quillraven.fleks.configureWorld
import io.sn.mywoods.event.MapChangeEvent
import io.sn.mywoods.fireEvent
import io.sn.mywoods.registerAllListener
import io.sn.mywoods.system.AnimationSystem
import io.sn.mywoods.system.EntitySpawnSystem
import io.sn.mywoods.system.RenderSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen(private val config: Map<String, Any>) : KtxScreen {

    private val stage = Stage(ExtendViewport(18f, 12f))
    private val objAtlas = TextureAtlas("graphics/gameObjects.atlas")
    private val aniAtlas = TextureAtlas("graphics/gameAnimation.atlas")
    private val mapAtlas = TextureAtlas("graphics/gameMapAshlands.atlas")
    private var currentMap: TiledMap? = null

    private val world = configureWorld {
        injectables {
            add("GameStage", stage)
        }

        systems {
            add(RenderSystem(stage))
            add(AnimationSystem(aniAtlas))
            add(EntitySpawnSystem())
        }
    }


    companion object {
        private val logger = logger<GameScreen>()
    }

    override fun show() {
        logger.debug { "GameScreen begins" }

        if (config.getOrDefault("fullscreen", "false") as Boolean) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        }

        world.registerAllListener(stage)

        currentMap = TmxMapLoader().load("maps/ashlands.tmx")
        stage.fireEvent(MapChangeEvent(currentMap!!))

        logger.debug { "GameScreen initialized" }
    }

    override fun resize(width: Int, height: Int) {
        //stage.viewport.update(width, height, true)
        //stage.camera.lookAt(0f, 0f, 0f)
    }

    override fun render(delta: Float) {
        world.update(delta)

        // fullscreen toggler
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) if (Gdx.graphics.isFullscreen) {
            (config.getOrDefault("resolution", "960x640") as String).split("x").map { it.toInt() }.let {
                Gdx.graphics.setWindowedMode(it[0], it[1])
            }
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        }

        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
    }

    override fun dispose() {
        stage.disposeSafely()
        currentMap?.disposeSafely()
        objAtlas.disposeSafely()
        aniAtlas.disposeSafely()
        mapAtlas.disposeSafely()
        world.dispose()
    }

}

