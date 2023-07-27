package io.sn.mywoods.screen

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.configureWorld
import io.sn.mywoods.component.AnimationComponent
import io.sn.mywoods.component.AnimationType
import io.sn.mywoods.component.ImageComponent
import io.sn.mywoods.system.AnimationSystem
import io.sn.mywoods.system.RenderSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen : KtxScreen {

    private val stage = Stage(ExtendViewport(16f, 9f))
    private val objAtlas = TextureAtlas("graphics/gameObjects.atlas")
    private val aniAtlas = TextureAtlas("graphics/gameAnimation.atlas")

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


        world.entity {
            it += ImageComponent().apply {
                image = Image(TextureRegion(objAtlas.findRegion("fighter"), 0, 0, 32, 48)).apply {
                    setPosition(1f, 5f)
                    setSize(2f, 2f)
                    setScaling(Scaling.fit)
                }
            }
            it += AnimationComponent("fighter", 2f, 1 / 8f, Animation.PlayMode.LOOP_REVERSED).apply {
                nextFrame("fighter", AnimationType.DOWN)
            }
        }

        world.entity {
            it += ImageComponent().apply {
                image = Image(TextureRegion(objAtlas.findRegion("slime"), 0, 0, 48, 48)).apply {
                    setPosition(6f, 5f)
                    setSize(1.5f, 1.5f)
                    setScaling(Scaling.fit)
                }
            }
            it += AnimationComponent("slime", 2f, 1 / 4f, Animation.PlayMode.LOOP_REVERSED).apply {
                nextFrame("slime", AnimationType.DOWN)
            }
        }

        logger.debug { "GameScreen initialized." }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        stage.disposeSafely()
        objAtlas.disposeSafely()
        world.dispose()
    }

}

