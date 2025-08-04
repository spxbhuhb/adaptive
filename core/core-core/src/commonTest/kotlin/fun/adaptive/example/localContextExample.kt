package `fun`.adaptive.example

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.findContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment

class ExampleContext(
    val content : String = "Hello World"
)

@Adaptive
fun localContextProvider() {
    localContext(ExampleContext()) {
        contextUnaware {
            contextConsumer()
        }
    }
}

@Adaptive
fun contextUnaware(
    content : @Adaptive () -> Unit
) {
    content()
}

@Adaptive
fun contextConsumer() {
    val context = fragment().findContext<ExampleContext>()
    println(context?.content)
}

