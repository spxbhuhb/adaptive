package `fun`.adaptive.ui.form

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContextOrNull
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.ui.input.InputViewBackend

open class FormViewBackend() {

    val inputBackends = mutableListOf<InputViewBackend<*>>()

    open fun <T, BT : InputViewBackend<T>> backendFor(
        binding: AdaptiveStateVariableBinding<T>?,
        newBackendFun: (value: T?, label: String?, secret: Boolean) -> BT
    ): BT {
        fun unbound() = newBackendFun(null, null, false)

        val companion = binding?.adatCompanion ?: return unbound()
        val path = binding.path?.toList() ?: return unbound()

        val property = companion.adatMetadata.properties.firstOrNull { it.name == path.lastOrNull() } ?: return unbound()

        val existing = inputBackends.firstOrNull { it.path == path }

        @Suppress("UNCHECKED_CAST")
        if (existing != null) return existing as BT

        newBackendFun(
            binding.value,
            path.lastOrNull(),
            property.isSecret(companion.adatDescriptors)
        ).also {
            it.isNullable = property.isNullable
            it.formBackend = this
            it.path = path
            inputBackends += it
            return it
        }
    }

    open fun onInputValueChange(inputBackend: InputViewBackend<*>) {

    }

    companion object {
        fun <T, BT : InputViewBackend<T>> AdaptiveFragment.viewBackendFor(
            binding : AdaptiveStateVariableBinding<T>?,
            newBackendFun: (value: T?, label: String?, secret: Boolean) -> BT
        ) : BT {
            return firstContextOrNull<FormViewBackend>()?.backendFor(binding, newBackendFun) ?: newBackendFun(null, null, false)
        }
    }

}