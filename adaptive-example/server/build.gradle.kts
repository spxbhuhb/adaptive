/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform")
    id("hu.simplexion.adaptive")

    java
    application
}

val baseName = "server"
val pomName = "Adaptive Example - Server"
val scmPath = "spxbhuhb/adaptive"

application {
    mainClass.set("MainKt")
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

    sourceSets["jvmMain"].dependencies {
        implementation("hu.simplexion.adaptive:adaptive-core:$version")
        implementation("hu.simplexion.adaptive:adaptive-exposed:$version")
        implementation("hu.simplexion.adaptive:adaptive-email:$version")
        implementation("hu.simplexion.adaptive:adaptive-browser:$version")
        implementation(project(":common"))
    }
}