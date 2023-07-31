package io.sn.mywoods.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.github.quillraven.fleks.configureWorld
import io.sn.mywoods.component.AnimationComponent
import io.sn.mywoods.component.AnimationModel
import io.sn.mywoods.component.AnimationType
import io.sn.mywoods.component.ImageComponent
import io.sn.mywoods.event.MapChangeEvent
import io.sn.mywoods.fireEvent
import io.sn.mywoods.registerAllListener
import io.sn.mywoods.system.AnimationSystem
import io.sn.mywoods.system.RenderSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen(private val config: Map<String, Any>) : KtxScreen {

    private val stage = Stage(ExtendViewport(16f, 9f))
    private val objAtlas = TextureAtlas("graphics/gameObjects.atlas")
    private val aniAtlas = TextureAtlas("graphics/gameAnimation.atlas")
    private val mapAtlas = TextureAtlas("graphics/gameMap.atlas")
    private var currentMap: TiledMap? = null

    private val world = configureWorld {
        injectables {
            add("GameStage", stage)
        }

        systems {
            add(RenderSystem(stage))
            add(AnimationSystem(aniAtlas))
        }
    }


    companion object {
        private val logger = logger<GameScreen>()
    }

    override fun show() {
        logger.debug { "GameScreen begins." }

        if (config.getOrDefault("fullscreen", "false") as Boolean) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        }

        world.registerAllListener(stage)

        currentMap = TmxMapLoader().load("maps/woods.tmx")
        stage.fireEvent(MapChangeEvent(currentMap!!))

        world.entity {
            it += ImageComponent().apply {
                image = Image(TextureRegion(objAtlas.findRegion("fighter"), 0, 0, 32, 48)).apply {
                    setPosition(1f, 5f)
                    setSize(2f, 2f)
                    setScaling(Scaling.fit)
                }
            }
            it += AnimationComponent(AnimationModel.FIGHTER, 0f, 1 / 4f, Animation.PlayMode.LOOP_REVERSED).apply {
                nextFrame(AnimationModel.FIGHTER, AnimationType.DOWN)
            }
        }

        world.entity {
            it += ImageComponent().apply {
                image = Image(TextureRegion(objAtlas.findRegion("slime"), 0, 0, 48, 48)).apply {
                    setPosition(6f, 5f)
                    setSize(1f, 1f)
                    setScaling(Scaling.fit)
                }
            }
            it += AnimationComponent(AnimationModel.SLIME, 0f, 1 / 4f, Animation.PlayMode.LOOP_REVERSED).apply {
                nextFrame(AnimationModel.SLIME, AnimationType.DOWN)
            }
        }

        world.entity {
            it += ImageComponent().apply {
                image = Image(TextureRegion(objAtlas.findRegion("chest_down"), 0, 0, 32, 48)).apply {
                    setPosition(4f, 3f)
                    setSize(1.5f, 1.5f)
                    setScaling(Scaling.fit)
                }
            }
            it += AnimationComponent(AnimationModel.CHEST_DOWN, 0f, 1 / 8f, Animation.PlayMode.NORMAL).apply {
                nextFrame(AnimationModel.CHEST_DOWN, AnimationType.LEFT)
            }
        }

        logger.debug { "GameScreen initialized." }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)

        // fullscreen toggler
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) if (Gdx.graphics.isFullscreen) {
            (config.getOrDefault("resolution", "1280x720") as String).split("x").map { it.toInt() }.let {
                Gdx.graphics.setWindowedMode(it[0], it[1])
            }
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        }


        // camera toggler
        /*
        val sviewport = (stage.viewport as ScalingViewport)
        if (Gdx.input.isKeyJustPressed(Input.Keys.F10)) if (sviewport.scaling == Scaling.fit) {
            sviewport.scaling = Scaling.fill
        } else {
            sviewport.scaling = Scaling.fit
        }
        */
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

