package `fun`.adaptive.adat

/**
 * When this annotation is added to a function, the compiler plugin
 * sets the values of parameters which are of type `AdatCompanion<A>`
 * to the companion object of the adat class specified by the type
 * parameter of the given function, if the parameter value is not
 * passed directly in the call.
 *
 * ```kotlin
 * @AdatCompanionResolve
 * fun <A : AdatClass> someFun(
 *      companion : AdatCompanion<A>? = null
 * )
 * ```
 *
 * This is a version of `reified` which works on non-inline functions as well.
 * The caller of the function has to use an actual class for this function
 * to work. The following examples **DOES NOT** work:
 *
 * ```kotlin
 * fun <B : AdatClass> someOtherFun() {
 *      someFun<B>()
 * }
 * ```
 *
 * When you use a function marked with [AdatCompanionResolve] with a non-resolvable
 * type parameter such as `B` in the example below, you'll get `null`. For some cases
 * (like autoItem here) this will throw a runtime error as AutoItem actually needs a
 * companion class. For some other cases this can work.
 *
 * TODO clarify the cases when @AdatCompanionResolve works and maybe add a FIR checker with warnings
 *
 * ```kotlin
 * class SomeClass<B> {
 *     val item = autoItem<B>(/* ... */)
 * }
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
annotation class AdatCompanionResolve()
