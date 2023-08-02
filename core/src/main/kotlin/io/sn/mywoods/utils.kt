@file:Suppress("unused")

package io.sn.mywoods

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.World
import com.google.common.base.CaseFormat
import com.google.common.base.Strings.nullToEmpty
import java.util.*


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

object CaseSupport {

    /**
     * @param name
     * @return 将变量名转为蛇形命名法格式的字符串
     */
    fun toSnakecase(name: String): String {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)
    }

    /**
     * @param name
     * @return 将变量名转为驼峰命名法格式的字符串
     */
    fun toCamelcase(name: String): String {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name)
    }

    /**
     * 判断 变量是否为驼峰命名法格式的字符串
     * @param input
     */
    fun isCamelcase(input: String): Boolean {
        return if (nullToEmpty(input).trim { it <= ' ' }.isNotEmpty()) {
            (input != input.lowercase(Locale.getDefault())
                    && input != input.uppercase(Locale.getDefault()) && input.indexOf('_') < 0)
        } else false
    }

    /**
     * 判断 变量是否为驼峰命名法格式的字符串
     * @param input
     */
    fun isSnakelcase(input: String): Boolean {
        return if (nullToEmpty(input).trim { it <= ' ' }.isNotEmpty()) {
            input.indexOf('_') >= 0
        } else false
    }
}

