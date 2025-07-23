/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin

import `fun`.adaptive.kotlin.runners.AbstractJvmBoxTest
import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5

fun main() {
    generateTestGroupSuiteWithJUnit5 {
        testGroup(testDataRoot = "testData", testsRoot = "test-gen") {

            testClass<AbstractJvmBoxTest> {
                model("box")
            }

            testClass<AbstractJvmBoxTest> {
                model("adhoc")
            }

        }
    }
}