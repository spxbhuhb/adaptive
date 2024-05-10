/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform") version "1.9.10" apply false
    id("hu.simplexion.adaptive") version "2024.05.07-SNAPSHOT" apply false
}

subprojects {
    group = "hu.simplexion.adaptive.example"
    repositories {
        mavenCentral()
        mavenLocal()
    }
}