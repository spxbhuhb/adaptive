/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    java
    signing
    `maven-publish`
}

group = "hu.simplexion.adaptive"
val baseName = "adaptive-core"
val pomName = "Adaptive Core"
val scmPath = "spxbhuhb/adaptive"

val coroutines_version: String by project
val datetime_version: String by project

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    js(IR) {
        browser()
        binaries.library()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetime_version")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
            }
        }
    }
}

// this is here for the compiler plugin box tests
tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
}

// ----------------------------------------------------------------
// DO NOT EDIT BELOW THIS, ASK FIRST!
// ----------------------------------------------------------------

val publishSnapshotUrl = (System.getenv("ADAPTIVE_PUBLISH_SNAPSHOT_URL") ?: project.findProperty("adaptive.publish.snapshot.url"))?.toString()
val publishReleaseUrl = (System.getenv("ADAPTIVE_PUBLISH_RELEASE_URL") ?: project.findProperty("adaptive.publish.release.url"))?.toString()
val publishUsername = (System.getenv("ADAPTIVE_PUBLISH_USERNAME") ?: project.findProperty("adaptive.publish.username"))?.toString()
val publishPassword = (System.getenv("ADAPTIVE_PUBLISH_PASSWORD") ?: project.findProperty("adaptive.publish.password"))?.toString()
val isSnapshot = "SNAPSHOT" in project.version.toString()
val isPublishing = project.properties["adaptive.publish"] != null || System.getenv("ADAPTIVE_PUBLISH") != null

publishing.publications.withType<MavenPublication> {
    val publication = this
    val javadocJar = tasks.register("${publication.name}JavadocJar", Jar::class) {
        archiveClassifier.set("javadoc")
        // Each archive name should be distinct. Mirror the format for the sources Jar tasks.
        archiveBaseName.set("${archiveBaseName.get()}-${publication.name}")
    }
    artifact(javadocJar)
}

fun RepositoryHandler.mavenRepo(repoUrl: String) {
    maven {
        url = project.uri(repoUrl)
        name = "Central"
        isAllowInsecureProtocol = true
        credentials {
            username = publishUsername
            password = publishPassword
        }
    }
}

if (isPublishing) {
    tasks.withType<Jar> {
        manifest {
            attributes += sortedMapOf(
                "Built-By" to System.getProperty("user.name"),
                "Build-Jdk" to System.getProperty("java.version"),
                "Implementation-Vendor" to "Simplexion Kft.",
                "Implementation-Version" to archiveVersion.get(),
                "Created-By" to GradleVersion.current()
            )
        }
    }

    signing {
        useGpgCmd()
        sign(publishing.publications)
    }

    publishing {

        repositories {
            if (isSnapshot) {
                requireNotNull(publishSnapshotUrl) { throw IllegalStateException("publishing: missing snapshot url, define ADAPTIVE_PUBLISH_SNAPSHOT_URL") }
                mavenRepo(publishSnapshotUrl)
            } else {
                requireNotNull(publishReleaseUrl) { throw IllegalStateException("publishing: missing release url, define ADAPTIVE_PUBLISH_RELEASE_URL") }
                mavenRepo(publishReleaseUrl)
            }
        }

        publications.withType<MavenPublication>().all {

            pom {
                description.set(project.name)
                name.set(pomName)
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
    }
}