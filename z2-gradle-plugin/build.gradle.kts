/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    id("com.gradle.plugin-publish") version "1.1.0"
    kotlin("jvm") version "1.9.10"
    signing
    `maven-publish`
}

kotlin {
    jvmToolchain(11)
}

repositories {
    mavenCentral()
}

group = "hu.simplexion.z2"

val scmPath = "spxbhuhb/z2"
val pomName = "Z2 Gradle Plugin"
val pluginDescription = "Kotlin Multiplatform compiler plugin for the Z2 library."

dependencies {
    implementation(kotlin("gradle-plugin-api"))
}

gradlePlugin {
    website.set("https://github.com/spxbhuhb/z2")
    vcsUrl.set("https://github.com/spxbhuhb/z2.git")
    plugins {
        create("z2c") {
            id = "hu.simplexion.z2"
            displayName = pomName
            description = pluginDescription
            implementationClass = "hu.simplexion.z2.gradle.Z2GradlePlugin"
            tags.set(listOf("kotlin"))
        }
    }
}


val String.propValue
    get() = (System.getenv(this.uppercase().replace('.', '_')) ?: project.findProperty(this))?.toString() ?: ""

val publishSnapshotUrl = "z2.publish.snapshot.url".propValue
val publishReleaseUrl = "z2.publish.release.url".propValue
val publishUsername = "z2.publish.username".propValue
val publishPassword = "z2.publish.password".propValue
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