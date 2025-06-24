package `fun`.adaptive.ui.form

import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContextOrNull
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.resource.ResourceKey
import `fun`.adaptive.resource.resolve.resolveString
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.ui.input.InputViewBackend

open class FormViewBackend() {

    var isFormDisabled: Boolean = false

    var application: AbstractApplication<*,*>? = null

    val inputBackends = mutableListOf<InputViewBackend<*, *>>()

    open fun <T, BT : InputViewBackend<T, BT>> backendFor(
        binding: AdaptiveStateVariableBinding<T>?,
        newBackendFun: (value: T?, label: String?, secret: Boolean) -> BT
    ): BT {
        fun unbound() = newBackendFun(null, null, false)

        val companion = binding?.adatCompanion ?: return unbound()
        val path = binding.path?.toList() ?: return unbound()

        val (propertyContainerInstance, propertyMetadata) = getProperty(companion, path) ?: return unbound()

        val existing = inputBackends.firstOrNull { it.path == path }

        @Suppress("UNCHECKED_CAST")
        if (existing != null) return existing as BT

        newBackendFun(
            getValue(binding, propertyMetadata),
            findLabel(binding.targetFragment, path.lastOrNull()),
            propertyMetadata.isSecret((propertyContainerInstance?.adatCompanion ?: companion).adatDescriptors)
        ).also { backend ->

            backend.isNullable = propertyMetadata.isNullable
            backend.isFormDisabled = isFormDisabled
            backend.formBackend = this
            backend.path = path

            binding.targetFragment.instructions.forEach { instruction ->
                instruction.applyTo(backend)
            }

            inputBackends += backend

            return backend
        }
    }

    open fun getProperty(companion: AdatCompanion<*>, path: List<String>) =
        companion.adatMetadata.getPropertyMetadataOrNull(path)

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

    open fun disableAll() {
        for (input in inputBackends) {
            isFormDisabled = true
            input.isFormDisabled = true
        }
    }

    open fun enableAll() {
        for (input in inputBackends) {
            isFormDisabled = false
            input.isFormDisabled = false
        }
    }

    fun findLabel(fragment: AdaptiveFragment, key: ResourceKey?): String? {
        if (key == null) return null

        val app = application
            ?: fragment.adapter.application.also { application = it }
            ?: return key

        return app.resolveString(key)
    }

    companion object {
        fun <T, BT : InputViewBackend<T, BT>> AdaptiveFragment.viewBackendFor(
            binding: AdaptiveStateVariableBinding<T>?,
            newBackendFun: (value: T?, label: String?, secret: Boolean) -> BT
        ): BT {
            return firstContextOrNull<FormViewBackend>()?.backendFor(binding, newBackendFun) ?: newBackendFun(null, null, false)
        }
    }

}