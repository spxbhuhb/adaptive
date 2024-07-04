/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.call

/**
 * Then this annotation is present on a function:
 *
 * - if the function has a `String` parameter called `callSiteName`
 * - **AND** the parameter value is not specified (using the default value)
 *
 * The compiler plugin replaces the value of the parameter with fully qualified
 * name of the first named parent of the function call statement.
 *
 * Example:
 *
 * ```kotlin
 * package somePackage.someSubPackage
 *
 * @CallSiteName
 * fun test(callSiteName : String = "<unknown>") {
 *      println(callSiteName)
 * }
 *
 * fun hello() {
 *     test() // prints out "somePackage.someSubPackage.hello"
 *     test("some other text") // prints out "some other text"
 * }
 * ```
 */
annotation class CallSiteName
