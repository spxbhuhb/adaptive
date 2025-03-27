import org.gradle.kotlin.dsl.adaptive
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    alias(libs.plugins.shadow)
}

version = "0.25.327.1"

// this is ugly but I don't use JS dependencies anyway, 
// https://youtrack.jetbrains.com/issue/KT-50848/Kotlin-JS-inner-build-routines-are-using-vulnerable-NPM-dependencies-and-now-that-we-have-kotlin-js-store-github-audit-this
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().lockFileName = "skip-yarn-lock"
}

val mainClassName = "fun.adaptive.iot.cli.MainKt"

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
            mainClass = mainClassName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktor.client.websockets)

                implementation(libs.adaptive.core)
                implementation(libs.adaptive.ui)
                implementation(libs.adaptive.lib.ktor)
                implementation(libs.adaptive.lib.auth)
                implementation(libs.adaptive.lib.value)
                implementation(libs.adaptive.lib.ui)
                implementation(libs.adaptive.lib.util)

                implementation(libs.adaptive.iot.lib.core)
                implementation(libs.adaptive.iot.lib.zigbee)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

    }

}


// see: https://stackoverflow.com/questions/63426211/kotlin-multiplatform-shadowjar-gradle-plugin-creates-empty-jar

fun registerShadowJar(targetName: String) {
    kotlin.targets.named<KotlinJvmTarget>(targetName) {
        compilations.named("main") {
            tasks {
                val shadowJar = register<ShadowJar>("${targetName}ShadowJar") {
                    group = "build"
                    from(output)
                    configurations = listOf(runtimeDependencyFiles)
                    archiveAppendix.set(targetName)
                    archiveClassifier.set("all")
                    manifest {
                        attributes("Main-Class" to mainClassName)
                    }
                    mergeServiceFiles()
                }
                getByName("${targetName}Jar") {
                    finalizedBy(shadowJar)
                }
            }
        }
    }
}

registerShadowJar("jvm")

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}

tasks.register("release") {
    dependsOn("build", "jvmShadowJar")
    outputs.upToDateWhen { false }
    doLast {
        Files.write(Paths.get("$projectDir/build/version.txt"), "$version".encodeToByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        Files.createDirectories(Paths.get("$projectDir/build/app/$version"))
        ProcessBuilder("$projectDir/pack.sh", projectDir.toString(), version.toString())
            .start()
            .waitFor()
    }
}