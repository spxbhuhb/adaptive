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
    resources {
        packageOfResources = "fun.adaptive.sandbox.app.echo.generated.resources"
    }
}

skipYarnLock()

kotlin {

    jvmToolchain(11)

    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass = "fun.adaptive.sandbox.app.echo.MainKt"
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
                implementation(libs.adaptive.lib.app)
                implementation(libs.adaptive.lib.auth)
                implementation(libs.adaptive.lib.ktor)
                implementation(libs.adaptive.lib.util)
                implementation(libs.adaptive.lib.value)
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