/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.kotlinJvm)
    signing
    alias(libs.plugins.gradleMavenPublish)
    alias(libs.plugins.download)
    alias(libs.plugins.shadow)
}

kotlin {
    jvmToolchain(11)
}

repositories {
    google()
    mavenCentral()
    mavenLocal()
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()

val baseName = "adaptive-gradle-plugin"
val scmPath = "spxbhuhb/adaptive"
val pomName = "Adaptive Gradle Plugin"
val pluginDescription = "Kotlin Multiplatform compiler plugin for the Adaptive library."

val embeddedDependencies by configurations.creating {
    isTransitive = false
}

dependencies {
    // By default, Gradle resolves plugins only via Gradle Plugin Portal.
    // To avoid declaring an additional repo, all dependencies must:
    // 1. Either be provided by Gradle at runtime (e.g. gradleApi());
    // 2. Or be included and optionally relocated.
    // Use `embedded` helper to include a dependency.
    fun embedded(dep: Any) {
        compileOnly(dep)
        testCompileOnly(dep)
        embeddedDependencies(dep)
    }

    compileOnly(gradleApi())
    compileOnly(kotlin("gradle-plugin"))
    implementation(kotlin("gradle-plugin-api"))
    compileOnly(libs.plugin.android)
    compileOnly(libs.plugin.android.api)
    implementation(libs.brotli4j)

    runtimeOnly(libs.brotli4j.linux)
    runtimeOnly(libs.brotli4j.linux.aarch64)
    runtimeOnly(libs.brotli4j.macos)
    runtimeOnly(libs.brotli4j.macos.aarch64)
    runtimeOnly(libs.brotli4j.windows)

    embedded(libs.kotlinx.io)
    embedded(libs.adaptive.core)
}

val packagesToRelocate = listOf("de.undercouch", "com.squareup.kotlinpoet")

val shadow = tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    for (packageToRelocate in packagesToRelocate) {
        relocate(packageToRelocate, "fun.adaptive.gradle.internal.$packageToRelocate")
    }
    archiveBaseName.set("shadow")
    archiveClassifier.set("")
    archiveVersion.set("")
    configurations = listOf(embeddedDependencies)
    exclude("META-INF/gradle-plugins/de.undercouch.download.properties")
    exclude("META-INF/versions/**")
}

tasks.named<Jar>("jar") {
    dependsOn(shadow)
    from(zipTree(shadow.get().archiveFile))
    this.duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

gradlePlugin {
    website.set("https://github.com/spxbhuhb/adaptive")
    vcsUrl.set("https://github.com/spxbhuhb/adaptive.git")
    plugins {
        create("a2c") {
            id = "fun.adaptive"
            displayName = pomName
            description = pluginDescription
            implementationClass = "fun.adaptive.gradle.AdaptiveGradlePlugin"
            tags.set(listOf("kotlin"))
        }
    }
}

tasks.register("jvmTest") {
    // this is here so we can run it on all projects at once
}

// ====  Automatic update of the Kotlin plugin version  ========================================================

val fileWithVersion = file("${projectDir}/src/main/kotlin/fun/adaptive/gradle/AdaptiveGradlePlugin.kt")
val versionRegex = """const val PLUGIN_VERSION\s*=\s*\"(\d+\.\d+\.\d+(-SNAPSHOT)?)\"""".toRegex()

tasks.register("updateVersion") {
    doFirst {
        val sourceFile = fileWithVersion
        val fileContent = sourceFile.readText()

        val currentVersionMatch = versionRegex.find(fileContent)
        val currentVersion = currentVersionMatch?.groups?.get(1)?.value

        if (currentVersion != version) {
            val updatedContent = versionRegex.replace(fileContent, """const val PLUGIN_VERSION = "$version"""")
            sourceFile.writeText(updatedContent)
        }
    }
}

tasks["checkKotlinGradlePluginConfigurationErrors"].dependsOn("updateVersion")

// ====  Publishing  ========================================================

signing {
    useGpgCmd()
    sign(publishing.publications)
}

mavenPublishing {

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates("fun.adaptive", baseName, version.toString())

    pom {
        url.set("https://github.com/$scmPath")
        name.set(pomName)
        description.set(pluginDescription)
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