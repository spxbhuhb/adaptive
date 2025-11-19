package `fun`.adaptive.graphics.svg.api

import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

@AdaptiveExpect(`fun`.adaptive.graphics.svg.svg)
fun svg(resource: GraphicsResourceSet, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(resource, instructions)
}

@AdaptiveExpect(`fun`.adaptive.graphics.svg.svg)
fun svgOrImage(resource: GraphicsResourceSet, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(resource, instructions)
}