/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    java
    application
}

group = "hu.simplexion.adaptive"
version = libs.versions.adaptive.get()

val baseName = "adaptive-sandbox"
val pomName = "Adaptive Sandbox"
val scmPath = "spxbhuhb/adaptive"

application {
    mainClass.set("MainKt")
}

adaptive {
    pluginDebug = true
//    pluginLogDir = projectDir.toPath()
    resources {
        publicResClass = true
        packageOfResClass = "sandbox"
        generateResClass = always
    }
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
                implementation(libs.adaptive.ui.common)
                implementation(libs.adaptive.lib.sandbox)
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