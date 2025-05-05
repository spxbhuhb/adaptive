package `fun`.adaptive.ui.input.button

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.generated.resources.invalidFields
import `fun`.adaptive.ui.generated.resources.save
import `fun`.adaptive.ui.snackbar.warningNotification

@Adaptive
fun <T : AdatClass> saveFormButton(
    form: AdatFormViewBackend<T>,
    label: String = Strings.save,
    icon: GraphicsResourceSet? = null,
    invalidFun: (() -> Unit)? = null,
    saveFun: (T) -> Unit
): AdaptiveFragment {
    button(label, icon, ButtonViewBackend(label).also { it.isSubmit = true }) .. onClick {
        if (form.isInvalid(true)) {
            invalidFun?.invoke() ?: {
                warningNotification(Strings.invalidFields)
            }
        } else {
            saveFun(form.inputValue)
        }
    }
    return fragment()
}