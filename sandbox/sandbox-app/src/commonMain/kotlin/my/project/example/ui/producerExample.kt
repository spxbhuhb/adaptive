package my.project.example.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.adaptiveStoreFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.text

val valueStore = adaptiveStoreFor(12)

@Adaptive
fun exampleFun() {
    val observed = valueFrom { valueStore }
    text(observed) .. onClick { valueStore.value = valueStore.value + 1 }
}