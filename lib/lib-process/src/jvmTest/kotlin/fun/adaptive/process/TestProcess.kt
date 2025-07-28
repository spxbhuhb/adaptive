package `fun`.adaptive.process

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestProcess {

    @Test
    fun testRunCommandSuccess() {
        // Test with a simple echo command
        val result = runCommand("echo", listOf("Hello World"))
        assertEquals("Hello World", result)
    }
    
    @Test
    fun testRunCommandFailure() {
        // Test with a command that should fail
        assertFailsWith<RuntimeException> {
            runCommand("ls", listOf("/nonexistent_directory"))
        }
    }
}