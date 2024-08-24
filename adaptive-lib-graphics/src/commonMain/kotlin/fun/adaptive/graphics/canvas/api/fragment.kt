package `fun`.adaptive.graphics.canvas.api

import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.resource.DrawableResource

@AdaptiveExpect(canvas)
fun circle() {
    manualImplementation()
}

@AdaptiveExpect(canvas)
fun svg(resource : DrawableResource, vararg instructions: AdaptiveInstruction) {
    manualImplementation(resource, instructions)
}