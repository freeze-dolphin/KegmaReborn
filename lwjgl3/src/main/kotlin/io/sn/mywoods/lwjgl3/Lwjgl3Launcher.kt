@file:JvmName("Lwjgl3Launcher")

package io.sn.mywoods.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import io.sn.mywoods.MysticWoods
import org.yaml.snakeyaml.Yaml
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.InputStreamReader

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired()) return

    Lwjgl3Application(MysticWoods(), Lwjgl3ApplicationConfiguration().apply {
        with(File("config.yaml")) {
            if (!exists()) { // extract default configuration
                var line: String?
                val fw = FileWriter(this)
                val bufferedReader =
                    BufferedReader(InputStreamReader(Thread.currentThread().contextClassLoader.getResourceAsStream(this.name)!!))
                do {
                    line = bufferedReader.readLine()
                    if (line != null) {
                        fw.write(line)
                    }
                } while (line != null)
                fw.close()
            }

            val yaml = Yaml()
            val config = yaml.load(FileReader(this.path)) as Map<String, Any>

            setTitle("MysticWoods")
            setWindowedMode(640, 480)
            setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
            useVsync(config.getOrDefault("vsync", true) as Boolean)
        }
    })
}
