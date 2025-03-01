import org.gradle.kotlin.dsl.adaptive
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
}

adaptive {
    pluginDebug = false
    debugFilter = ".*"
    resources {
        publicAccessors = true
        packageOfResources = "fun.adaptive.sandbox"
    }
}

// this is ugly but I don't use JS dependencies anyway, 
// https://youtrack.jetbrains.com/issue/KT-50848/Kotlin-JS-inner-build-routines-are-using-vulnerable-NPM-dependencies-and-now-that-we-have-kotlin-js-store-github-audit-this
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().lockFileName = "skip-yarn-lock"
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    jvmToolchain(11)

    jvm {
        withJava()
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
                implementation(libs.adaptive.core)
                implementation(libs.adaptive.ui)
                implementation(libs.adaptive.lib.cookbook)
                implementation(libs.adaptive.lib.email)
                implementation(libs.adaptive.lib.ktor)
                implementation(libs.adaptive.lib.auth)
                implementation(libs.adaptive.lib.auto)
                implementation(libs.adaptive.lib.graphics)
                implementation(libs.adaptive.lib.document)
                implementation(libs.adaptive.lib.ui)
                implementation(libs.adaptive.grove)
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
                implementation(libs.h2database)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.netty)
                implementation(libs.ktor.server.websockets)
                implementation(libs.ktor.server.forwardedheaders)
                implementation(libs.adaptive.lib.exposed)
            }
        }
    }

}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}