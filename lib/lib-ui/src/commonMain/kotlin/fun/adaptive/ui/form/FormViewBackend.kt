package `fun`.adaptive.ui.form

import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContextOrNull
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.ui.input.InputViewBackend

open class FormViewBackend() {

    val inputBackends = mutableListOf<InputViewBackend<*, *>>()

    open fun <T, BT : InputViewBackend<T, BT>> backendFor(
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
            getValue(binding, property),
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

    /**
     * This is a tricky proposition. It is not guaranteed that the binding value is correct. If you use a template
     * for the accessor, the binding will contain the value of the template all the time.
     * Technically, with Adat we do not need the binding, a simple property path would be enough.
     *
     * TODO think about template/property accessor in FormViewBackend
     */
    open fun <T> getValue(
        binding: AdaptiveStateVariableBinding<T>,
        property: AdatPropertyMetadata
    ): T {
        return binding.value
    }

    open fun onInputValueChange(inputBackend: InputViewBackend<*, *>) {

    }

    companion object {
        fun <T, BT : InputViewBackend<T, BT>> AdaptiveFragment.viewBackendFor(
            binding: AdaptiveStateVariableBinding<T>?,
            newBackendFun: (value: T?, label: String?, secret: Boolean) -> BT
        ): BT {
            println("====: ${(firstContextOrNull<FormViewBackend>() as? AdatFormViewBackend<*>)?.value}")
            return firstContextOrNull<FormViewBackend>()?.backendFor(binding, newBackendFun) ?: newBackendFun(null, null, false)
        }
    }

}