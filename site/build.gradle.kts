/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    java
    application
}

application {
    mainClass.set("MainKt")
}

adaptive {
    pluginDebug = false
    resources {
        publicResClass = true
        packageOfResClass = "hu.simplexion.adaptive.site"
        generateResClass = auto
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
    }

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.adaptive.core)
                implementation(libs.adaptive.lib.email)
                implementation(libs.adaptive.lib.ktor)
                implementation(libs.adaptive.ui)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.adaptive.lib.exposed)
            }
        }
    }

}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}