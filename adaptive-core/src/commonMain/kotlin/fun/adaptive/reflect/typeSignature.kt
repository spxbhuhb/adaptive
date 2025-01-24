package `fun`.adaptive.reflect

import `fun`.adaptive.foundation.replacedByPlugin

/**
 * Get the **COMPILE TIME** WireFormat type signature of the receiver.
 *
 * The following code prints out `LI;` twice. At compile time type of `a` is `I`,
 * no matter what instance you store in it.
 *
 * ```kotlin
 * interface I
 * class A : I
 * class B : I
 *
 * fun main() {
 *     var a : I = A()
 *     println(a.typeSignature)
 *     a = B()
 *     println(a.typeSignature)
 * }
 * ```
 */
fun Any?.typeSignature() : String {
    replacedByPlugin("replaced with the type signature of the extension receiver")
}

/**
 * Get the **COMPILE TIME** WireFormat type signature of the receiver.
 *
 * Variant for the cases when we do not have an instance.
 */
fun <T> typeSignature() : String {
    replacedByPlugin("replaced with the type signature of the extension receiver")
}