@file:JvmName("Lwjgl3Launcher")

package io.sn.mywoods.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import io.sn.mywoods.MysticWoods
import org.yaml.snakeyaml.Yaml
import java.io.*

/** Launches the desktop (LWJGL3) application. */

fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired()) return

    with(File("config.yaml")) {
        if (!exists()) { // extract default configuration
            var line: String?
            val fw = FileWriter(this)
            val bufferedReader =
                BufferedReader(InputStreamReader(Thread.currentThread().contextClassLoader.getResourceAsStream(this.name)!!))
            do {
                line = bufferedReader.readLine()
                if (line != null) {
                    fw.write("$line\n")
                }
            } while (line != null)
            fw.close()
        }

        val yaml = Yaml()
        val config = yaml.load(FileReader(this.path)) as Map<String, Any>
        Lwjgl3Application(MysticWoods(config), Lwjgl3ApplicationConfiguration().apply {
            setTitle("MysticWoods")

            (config.getOrDefault("resolution", "1280x720") as String).split("x").map { it.toInt() }.let {
                setWindowedMode(it[0], it[1])
            }
            //setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
            useVsync(config.getOrDefault("vsync", true) as Boolean)
        })
    }
}
