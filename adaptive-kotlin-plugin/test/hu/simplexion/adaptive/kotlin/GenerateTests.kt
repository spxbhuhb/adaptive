/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin

import hu.simplexion.adaptive.kotlin.runners.AbstractAdhocTest
import hu.simplexion.adaptive.kotlin.runners.AbstractBoxTest
import hu.simplexion.adaptive.kotlin.runners.AbstractDiagnosticTest
import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5

fun main() {
    generateTestGroupSuiteWithJUnit5 {
        testGroup(testDataRoot = "testData", testsRoot = "test-gen") {

            testClass<AbstractDiagnosticTest> {
                model("diagnostics")
            }

            testClass<AbstractBoxTest> {
                model("box")
            }

            testClass<AbstractAdhocTest> {
                model("adhoc")
            }

        }
    }
}