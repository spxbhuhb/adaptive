import org.gradle.kotlin.dsl.adaptive

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()

val baseName = "doc-example"
val pomName = "Adaptive Documentation Examples"
val scmPath = "spxbhuhb/adaptive"

adaptive {
    resources {
        publicAccessors = true
        packageOfResources = "fun.adaptive.doc.example.generated.resources"
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

    jvm()

    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.adaptive.core.core)
                implementation(libs.adaptive.core.ui)
                implementation(libs.adaptive.lib.app)
                implementation(libs.adaptive.lib.ktor)
                implementation(libs.adaptive.lib.auth)
                implementation(libs.adaptive.lib.graphics)
                implementation(libs.adaptive.lib.document)
                implementation(libs.adaptive.lib.ui)
                implementation(libs.adaptive.lib.util)
                implementation(libs.adaptive.lib.value)
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