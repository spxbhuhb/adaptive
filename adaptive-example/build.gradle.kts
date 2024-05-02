/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform") version "1.9.10"
    id("hu.simplexion.adaptive") version "2024.05.02"

    java
    application
}

group = "hu.simplexion.adaptive"
val baseName = "adaptive-example"
val pomName = "Adaptive Example"
val scmPath = "spxbhuhb/adaptive"

repositories {
    mavenCentral()
}

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

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets["commonMain"].dependencies {
        implementation("hu.simplexion.adaptive:adaptive-core:$version")
        implementation("hu.simplexion.adaptive:adaptive-exposed:$version")
        implementation("hu.simplexion.adaptive:adaptive-email:$version")
        implementation("hu.simplexion.adaptive:adaptive-browser:$version")
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}