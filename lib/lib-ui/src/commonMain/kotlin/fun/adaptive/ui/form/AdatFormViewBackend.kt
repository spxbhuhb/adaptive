package `fun`.adaptive.ui.form

import `fun`.adaptive.adat.AdatChange
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.PropertyPath
import `fun`.adaptive.adat.api.deepCopy
import `fun`.adaptive.adat.api.validate
import `fun`.adaptive.adat.descriptor.InstanceValidationResult
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.general.Observable
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.ui.input.InputViewBackend
import kotlin.reflect.KProperty0

class AdatFormViewBackend<T : AdatClass>(
    initialValue: T,
    var validateFun: AdatFormViewBackend<T>.(it: T) -> Unit = { }
) : FormViewBackend(), Observable<T> {

    override var value: T = initialValue
        set(value) {
            field = value
            notifyListeners()
        }

    override val listeners = mutableListOf<ObservableListener<T>>()

    val specificValidationResult = mutableListOf<PropertyPath>()
    var descriptorValidationResult = InstanceValidationResult()
    val failPaths = mutableSetOf<PropertyPath>()

    init {
        // Initialize `failPaths` so `backendFor` can set `isInConstraintError` if needed.
        validate(value)
    }

    override fun <T, BT : InputViewBackend<T, BT>> backendFor(
        binding: AdaptiveStateVariableBinding<T>?,
        newBackendFun: (value: T?, label: String?, secret: Boolean) -> BT
    ): BT {
        return super.backendFor(binding, newBackendFun).also {
            if (it.path in failPaths) it.isInConstraintError = true
        }
    }

    override fun <T> getValue(binding: AdaptiveStateVariableBinding<T>, property: AdatPropertyMetadata): T {
        @Suppress("UNCHECKED_CAST")
        return value.getValue(property.name) as T
    }

    override fun onInputValueChange(inputBackend: InputViewBackend<*, *>) {
        val newValue = value.deepCopy(AdatChange(inputBackend.path, inputBackend.inputValue))

        validate(newValue)

        for (backend in inputBackends) {
            if (backend.path in failPaths) {
                if (! backend.isInConstraintError) backend.isInConstraintError = true
            } else {
                if (backend.isInConstraintError) backend.isInConstraintError = false
            }
        }

        value = newValue
    }

    fun validate(newValue: T) {

        failPaths.clear()

        // First, execute the form-specific validation. This puts paths of failed
        // properties into failPaths.

        specificValidationResult.clear()
        validateFun(newValue)
        failPaths += specificValidationResult

        // Then, run the Adat descriptor-based validation. Add the failed paths
        // to failPaths as well.

        descriptorValidationResult = newValue.validate()

        // We have to check and update all properties as it is possible that the change of
        // one property affects another one.

        for (fail in descriptorValidationResult.failedConstraints) {
            val property = fail.property ?: continue
            val path = listOf(property.name)
            failPaths += path
        }
    }

    operator fun get(property: KProperty0<*>): InputViewBackend<*, *>? {
        val path = listOf(property.name)
        return inputBackends.firstOrNull { it.path == path }
    }

    fun expectEquals(p1: KProperty0<*>, p2: KProperty0<*>, dualTouch: Boolean) {
        if (p1.get() == p2.get()) return

        val touch1 = this[p1]?.isTouched ?: true
        val touch2 = this[p2]?.isTouched ?: true

        if (! dualTouch || (touch1 && touch2)) {
            specificValidationResult += listOf(p1.name)
            specificValidationResult += listOf(p2.name)
        }
    }

    fun isInvalid(touchAll: Boolean): Boolean {
        val valid = specificValidationResult.isEmpty() && descriptorValidationResult.isValid
        if (touchAll) {
            inputBackends.forEach {
                it.isTouched = true
                it.notifyListeners() // isTouched is not observed, so we have to force a notification.
            }
        }
        return ! valid
    }

}