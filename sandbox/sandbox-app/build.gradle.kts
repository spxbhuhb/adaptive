import `fun`.adaptive.internal.gradle.skipYarnLock
import org.gradle.kotlin.dsl.adaptive
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    id("fun.adaptive.internal.gradle")
}

adaptive {
    pluginDebug = false
    debugFilter = ".*"
    resources {
        publicAccessors = true
        packageOfResources = "fun.adaptive.sandbox.app.generated.resources"
    }
}

skipYarnLock()

kotlin {

    jvmToolchain(11)

    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass = "fun.adaptive.sandbox.MainKt"
        }
    }

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.adaptive.core.core)
                implementation(libs.adaptive.core.ui)

                implementation(libs.adaptive.doc.example)

                implementation(libs.adaptive.lib.app)
                implementation(libs.adaptive.lib.ktor)
                implementation(libs.adaptive.lib.chart)
                implementation(libs.adaptive.lib.document)
                implementation(libs.adaptive.lib.auth)
                implementation(libs.adaptive.lib.graphics)
                implementation(libs.adaptive.lib.ui)
                implementation(libs.adaptive.lib.ui.mpw)
                implementation(libs.adaptive.lib.util)
                implementation(libs.adaptive.lib.value)

                implementation(libs.adaptive.grove.lib)
                implementation(libs.adaptive.grove.runtime)

                implementation(libs.kotlinx.coroutines.debug)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        jsMain {
            dependencies {
                implementation(libs.ktor.client.core)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.netty)
                implementation(libs.ktor.server.websockets)
                implementation(libs.ktor.server.forwardedheaders)
            }
        }
    }

}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}