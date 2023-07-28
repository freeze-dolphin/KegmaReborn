package io.sn.mywoods.system

import io.sn.mywoods.system.AnimationSystem.Companion.nextRotation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AnimationSystemTest {

    @Test
    fun testNextRotation() {
        val orig = "up"
        assertEquals("left", nextRotation(orig))
    }

}
