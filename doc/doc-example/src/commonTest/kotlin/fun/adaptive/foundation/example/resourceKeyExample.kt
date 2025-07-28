package `fun`.adaptive.foundation.example

import `fun`.adaptive.resource.resolve.resolveString
import `fun`.adaptive.resource.resourceKey
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.ui.generated.resources.example

fun resourceKeyExample(
    application: AbstractApplication<*,*>
) {
    val messageKey = resourceKey(Strings::example)

    // prints out "example", the key that belongs to the resource

    println(messageKey)

    val message = application.resolveString(messageKey)

    // prints out the translated text, for example, if the
    // locale is Hungarian, the output is "Példa"

    println(message)
}