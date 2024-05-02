/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform") version "1.9.10"
    id("hu.simplexion.adaptive") version "2024.04.19"

    java
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "hu.simplexion.adaptive"
val baseName = "adaptive-site"
val pomName = "Adaptive Site"
val scmPath = "spxbhuhb/adaptive"

tasks["build"].dependsOn(gradle.includedBuilds.map { it.task(":build") })
tasks["clean"].dependsOn(gradle.includedBuilds.map { it.task(":clean") })

val ktor_version: String by project
val logback_version: String by project

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

application {
    mainClass.set("hu.simplexion.adaptive.site.MainKt")
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
    sourceSets["jsMain"].dependencies {
        implementation("io.ktor:ktor-client-websockets:$ktor_version")
    }
    sourceSets["jvmMain"].dependencies {
        implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-websockets:$ktor_version")

        implementation("ch.qos.logback:logback-classic:$logback_version")

        implementation("org.postgresql:postgresql:42.2.2")
        implementation("com.h2database:h2:2.1.214")
        implementation("com.zaxxer:HikariCP:3.4.2")
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}