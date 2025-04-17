import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import `fun`.adaptive.gradle.js.CompressJsResourcesTask
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.text.SimpleDateFormat
import java.util.*

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    alias(libs.plugins.shadow)
}

version = libs.versions.adaptive.get()

adaptive {
    pluginDebug = false
    resources {
        publicAccessors = true
        withFileQualifiers = false
        trace = false
        packageOfResources = "fun.adaptive.site.app.generated.resources"
    }
}

// this is ugly but I don't use JS dependencies anyway, 
// https://youtrack.jetbrains.com/issue/KT-50848/Kotlin-JS-inner-build-routines-are-using-vulnerable-NPM-dependencies-and-now-that-we-have-kotlin-js-store-github-audit-this
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().lockFileName = "skip-yarn-lock"
}

val mainClassName = "MainKt"

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    jvmToolchain(11)

    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass = mainClassName
        }
    }

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.adaptive.core.core)
                implementation(libs.adaptive.core.ui)

                implementation(libs.adaptive.grove.lib)
                implementation(libs.adaptive.grove.runtime)

                implementation(libs.adaptive.site.lib.cookbook)
                implementation(libs.adaptive.lib.ktor)
                implementation(libs.adaptive.lib.auth)
                implementation(libs.adaptive.lib.document)
                implementation(libs.adaptive.lib.graphics)
                implementation(libs.adaptive.lib.value)
                implementation(libs.adaptive.lib.util)
                implementation(libs.adaptive.lib.ui)
                implementation(libs.adaptive.lib.app)
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

// see: https://stackoverflow.com/questions/63426211/kotlin-multiplatform-shadowjar-gradle-plugin-creates-empty-jar
val defaultMainClassName: String = "MainKt"

fun registerShadowJar(targetName: String, mainClassName: String? = defaultMainClassName) {
    kotlin.targets.named<KotlinJvmTarget>(targetName) {
        compilations.named("main") {
            tasks {
                val shadowJar = register<ShadowJar>("${targetName}ShadowJar") {
                    group = "build"
                    from(output)
                    configurations = listOf(runtimeDependencyFiles)
                    archiveAppendix.set(targetName)
                    archiveClassifier.set("all")
                    if (mainClassName != null) {
                        manifest {
                            attributes("Main-Class" to mainClassName)
                        }
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

tasks.getByName("build") {
    doFirst {
        val out = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "--short", "HEAD")
            standardOutput = out
        }

        val about = """
            version: $version
            releaseDate: ${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").also { it.timeZone = TimeZone.getTimeZone("UTC") }.format(Date())}
            commit: ${out.toByteArray().decodeToString()}
        """.trimIndent()

        Files.write(Paths.get("$projectDir/var/release/about.yaml"), about.encodeToByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
    }
}

tasks.register<CompressJsResourcesTask>("compressJsResources") {
    dependsOn("build")
}

tasks.named("jsBrowserProductionWebpack") {
    finalizedBy("compressJsResources")
}

tasks.register("release") {
    dependsOn("build", "jvmShadowJar", "compressJsResources")
    outputs.upToDateWhen { false }
    doLast {
        Files.write(Paths.get("$projectDir/build/version.txt"), "$version".encodeToByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        Files.createDirectories(Paths.get("$projectDir/build/app/$version"))
        ProcessBuilder("$projectDir/pack.sh", projectDir.toString(), version.toString())
            .start()
            .waitFor()
    }
}