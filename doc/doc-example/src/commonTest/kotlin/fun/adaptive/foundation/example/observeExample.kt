package `fun`.adaptive.foundation.example

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.text

val exampleInt = observableOf { 12 }

@Adaptive
fun exampleFun() {
    val observed = observe { exampleInt }
    text(observed) .. onClick { exampleInt.value = exampleInt.value + 1 }
}