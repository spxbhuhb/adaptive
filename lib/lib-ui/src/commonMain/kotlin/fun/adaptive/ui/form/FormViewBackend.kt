package `fun`.adaptive.ui.form

import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.ui.input.InputViewBackend

open class FormViewBackend() {

    val inputBackends = mutableListOf<InputViewBackend<*>>()

    open fun <T> backendFor(binding: AdaptiveStateVariableBinding<T>?): InputViewBackend<T>? {
        if (binding == null) return null
        val companion = binding.adatCompanion ?: return null
        val path = binding.path?.toList() ?: return null

        val property = companion.adatMetadata.properties.firstOrNull { it.name == path.lastOrNull() }
        if (property == null) return null

        val existing = inputBackends.firstOrNull { it.path == path }

        @Suppress("UNCHECKED_CAST")
        if (existing != null) return existing as InputViewBackend<T>?

        InputViewBackend(
            value = binding.value,
            label = path.lastOrNull(),
            isSecret = property.isSecret(companion.adatDescriptors)
        ).also {
            it.formBackend = this
            it.path = path
            inputBackends += it
            return it
        }
    }

    open fun onInputValueChange(inputBackend: InputViewBackend<*>) {

    }

}