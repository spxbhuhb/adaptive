/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinJvm)
    java
    signing
    alias(libs.plugins.gradleMavenPublish)
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()

val baseName = "core-kotlin-plugin"
val scmPath = "spxbhuhb/adaptive"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
}

kotlin {
    jvmToolchain(11) // this is not needed for 2.0.0-RC2, but have to have in 1.9.10
    compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    // this opt in is OK, according to:
    // https://kotlinlang.slack.com/archives/C7L3JB43G/p1700429910462239
    compilerOptions.freeCompilerArgs.add("-opt-in=org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI")
    compilerOptions.freeCompilerArgs.add("-opt-in=org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src"))
        resources.setSrcDirs(listOf("resources"))
    }
    test {
        java.setSrcDirs(listOf("test", "test-gen"))
        resources.setSrcDirs(listOf("testResources"))
    }
}

dependencies {
    compileOnly(libs.kotlin.compiler)
    implementation(libs.adaptive.core.core)

    testImplementation(libs.kotlin.compiler)

    testRuntimeOnly(libs.kotlin.test)
    testRuntimeOnly(libs.kotlin.script.runtime)
    testRuntimeOnly(libs.kotlin.annotations.jvm)

    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.kotlin.compiler.internal.test.framework)
    testImplementation(libs.junit)

    testRuntimeOnly(libs.kotlinx.coroutines.core)
    testRuntimeOnly(libs.kotlinx.datetime)
    testRuntimeOnly(libs.slf4j)
    testRuntimeOnly(libs.slf4j.nop)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-commons")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.platform:junit-platform-runner")
    testImplementation("org.junit.platform:junit-platform-suite-api")
}

// Copy `core-core` into the plugin JAR. This is necessary as the plugin
// uses classes and functions from the core and without this there is no way
// to provided them.
// TODO verify that only the necessary classes are copied into the plugin jar
tasks.jar {
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.WARN
}

tasks.test {
    testLogging.showStandardStreams = true
    useJUnitPlatform()
    doFirst {
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-stdlib", "kotlin-stdlib")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-stdlib-jdk8", "kotlin-stdlib-jdk8")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-reflect", "kotlin-reflect")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-test", "kotlin-test")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-script-runtime", "kotlin-script-runtime")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-annotations-jvm", "kotlin-annotations-jvm")
        setLibraryProperty("adaptive.kotlin.test.kotlinx-coroutines-core", "kotlinx-coroutines-core-jvm")
        setLibraryProperty("adaptive.kotlin.test.kotlinx-datetime", "kotlinx-datetime-jvm")
        setLibraryProperty("adaptive.org.slf4j:slf4j-api", "slf4j-api")
        setLibraryProperty("adaptive.org.slf4j:slf4j-nop", "slf4j-nop")
    }
}

tasks.create<JavaExec>("generateTests") {
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("fun.adaptive.kotlin.GenerateTestsKt")
}

fun Test.setLibraryProperty(propName: String, jarName: String) {
    val path = project.configurations
        .testRuntimeClasspath.get()
        .files
        .find { """$jarName-\d.*jar""".toRegex().matches(it.name) }
        ?.absolutePath
        ?: return
    systemProperty(propName, path)
}

tasks.register("jvmTest") {
    dependsOn(tasks["test"])
}

// ---- Publishing -----

signing {
    useGpgCmd()
    sign(publishing.publications)
}

mavenPublishing {

    publishToMavenCentral()

    signAllPublications()

    coordinates("fun.adaptive", baseName, version.toString())

    pom {
        description.set("Koltin compiler plugin for Adaptive")
        name.set(project.name)
        url.set("https://github.com/$scmPath")
        scm {
            url.set("https://github.com/$scmPath")
            connection.set("scm:git:git://github.com/$scmPath.git")
            developerConnection.set("scm:git:ssh://git@github.com/$scmPath.git")
        }
        licenses {
            license {
                name.set("Apache 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("toth-istvan-zoltan")
                name.set("Tóth István Zoltán")
                url.set("https://github.com/toth-istvan-zoltan")
                organization.set("Simplexion Kft.")
                organizationUrl.set("https://www.simplexion.hu")
            }
        }
    }
}
