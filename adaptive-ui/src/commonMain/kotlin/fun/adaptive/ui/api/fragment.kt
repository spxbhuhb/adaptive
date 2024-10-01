package `fun`.adaptive.ui.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.aui

@AdaptiveExpect(aui)
fun canvas(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun image(res: DrawableResource, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(res, instructions)
}

@AdaptiveExpect(aui)
fun input(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<String>? = null,
    @PropertySelector
    selector: () -> String
): AdaptiveFragment {
    manualImplementation(instructions, binding, selector)
}

@AdaptiveExpect(aui)
fun <T> boundInput(
    vararg instructions: AdaptiveInstruction,
    binding: AdaptiveStateVariableBinding<T>,
    toString: (T) -> String,
    fromString: (String) -> T?
): AdaptiveFragment {
    manualImplementation(instructions, binding)
}

@AdaptiveExpect(aui)
fun box(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun flowBox(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun rootBox(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun row(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun column(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun grid(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun space(vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(instructions)
}

@AdaptiveExpect(aui)
fun slot(vararg instructions: AdaptiveInstruction, @Adaptive initialContent: () -> Unit) {
    manualImplementation(instructions, initialContent)
}

@AdaptiveExpect(aui)
fun text(content: Any?, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(content, instructions)
}

@AdaptiveExpect(aui)
fun flowText(content: Any?, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(content, instructions)
}
