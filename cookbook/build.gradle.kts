import org.gradle.kotlin.dsl.adaptive

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
        packageOfResClass = "fun.adaptive.cookbook"
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
                implementation(libs.adaptive.ui)
                implementation(libs.adaptive.lib.email)
                implementation(libs.adaptive.lib.ktor)
                implementation(libs.adaptive.lib.auth)
                implementation(libs.adaptive.lib.auto)
                implementation(libs.adaptive.lib.graphics)
                implementation(libs.adaptive.lib.markdown)
                implementation(libs.kotlinx.coroutines.debug)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.h2database)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.netty)
                implementation(libs.ktor.server.websockets)
                implementation(libs.adaptive.lib.exposed)
            }
        }
    }

}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}