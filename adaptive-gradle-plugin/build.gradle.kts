import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.pluginPublish)
    alias(libs.plugins.kotlinJvm)
    signing
    `maven-publish`
}

kotlin {
    jvmToolchain(11)
}

repositories {
    google()
    mavenCentral()
}

group = "hu.simplexion.adaptive"
version = libs.versions.adaptive.get()

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

    embedded(libs.kotlinpoet)
}

gradlePlugin {
    website.set("https://github.com/spxbhuhb/adaptive")
    vcsUrl.set("https://github.com/spxbhuhb/adaptive.git")
    plugins {
        create("a2c") {
            id = "hu.simplexion.adaptive"
            displayName = pomName
            description = pluginDescription
            implementationClass = "hu.simplexion.adaptive.gradle.AdaptiveGradlePlugin"
            tags.set(listOf("kotlin"))
        }
    }
}

// ====  Automatic update of the Kotlin plugin version  ========================================================

val fileWithVersion = file("${projectDir}/src/main/kotlin/hu/simplexion/adaptive/gradle/AdaptiveGradlePlugin.kt")
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

val String.propValue
    get() = (System.getenv(this.uppercase().replace('.', '_')) ?: project.findProperty(this))?.toString() ?: ""

val publishSnapshotUrl = "adaptive.publish.snapshot.url".propValue
val publishReleaseUrl = "adaptive.publish.release.url".propValue
val publishUsername = "adaptive.publish.username".propValue
val publishPassword = "adaptive.publish.password".propValue
val isSnapshot = "SNAPSHOT" in project.version.toString()

signing {
    useGpgCmd()
    sign(publishing.publications)
}

publishing {

    repositories {
        maven {
            name = "MavenCentral"
            url = project.uri(requireNotNull(if (isSnapshot) publishSnapshotUrl else publishReleaseUrl))
            credentials {
                username = publishUsername
                password = publishPassword
            }
        }
    }

    publications.withType<MavenPublication>().all {
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
}