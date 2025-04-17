package `fun`.adaptive.ui.form

import `fun`.adaptive.adat.AdatClass

fun <T : AdatClass> adatFormBackend(
    initialValue: T,
    validateFun: AdatFormViewBackend<T>.(it: T) -> Unit = { }
) =
    AdatFormViewBackend(initialValue, validateFun)
