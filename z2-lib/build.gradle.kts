/*
 * Copyright © 2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform") version "1.9.10"
    id("hu.simplexion.z2") version "2024.04.19"
    java
    signing
    `maven-publish`
}

group = "hu.simplexion.z2"
val baseName = "z2-lib"
val pomName = "Z2 Lib"
val scmPath = "spxbhuhb/z2"

tasks["build"].dependsOn(gradle.includedBuild("z2-kotlin-plugin").task(":publishToMavenLocal"))
tasks["build"].dependsOn(gradle.includedBuilds.map { it.task(":build") })
tasks["clean"].dependsOn(gradle.includedBuilds.map { it.task(":clean") })
tasks["publishToMavenLocal"].dependsOn(gradle.includedBuilds.map { it.task(":publishToMavenLocal") })
tasks["publish"].dependsOn(gradle.includedBuilds.map { it.task(":publish") })

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

val coroutines_version: String by project
val datetime_version: String by project

val ktor_version: String by project
val logback_version: String by project
val exposed_version: String by project
val javamail_version: String by project

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
                api("io.ktor:ktor-client-websockets:$ktor_version")
                api("org.jetbrains.kotlinx:kotlinx-datetime:$datetime_version")

                api("hu.simplexion.z2:z2-core:${version}")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        sourceSets["jvmMain"].dependencies {
            api("io.ktor:ktor-server-core-jvm:$ktor_version")
            api("io.ktor:ktor-server-netty-jvm:$ktor_version")
            api("io.ktor:ktor-server-websockets:$ktor_version")

            api("org.jetbrains.exposed:exposed-core:${exposed_version}")
            api("org.jetbrains.exposed:exposed-jdbc:${exposed_version}")
            api("org.jetbrains.exposed:exposed-kotlin-datetime:${exposed_version}")

            api("com.zaxxer:HikariCP:3.4.2")

            api("ch.qos.logback:logback-classic:${logback_version}")
            api("org.apache.logging.log4j:log4j-core:2.20.0") // FFS

            api("com.sun.mail:javax.mail:${javamail_version}")

            implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")

            implementation("org.docx4j:docx4j-JAXB-ReferenceImpl:11.4.7") {
                exclude("org.slf4j", "slf4j-api")
            }
            implementation("org.docx4j:docx4j-export-fo:11.4.7") {
                exclude("org.slf4j", "slf4j-api")
            }
        }

        sourceSets["jvmTest"].dependencies {
            implementation(kotlin("test"))
            implementation("com.h2database:h2:2.1.214")
            implementation("org.subethamail:subethasmtp:3.1.7")
        }
    }
}

// ----------------------------------------------------------------
// DO NOT EDIT BELOW THIS, ASK FIRST!
// ----------------------------------------------------------------

val publishSnapshotUrl = (System.getenv("Z2_PUBLISH_SNAPSHOT_URL") ?: project.findProperty("z2.publish.snapshot.url"))?.toString()
val publishReleaseUrl = (System.getenv("Z2_PUBLISH_RELEASE_URL") ?: project.findProperty("z2.publish.release.url"))?.toString()
val publishUsername = (System.getenv("Z2_PUBLISH_USERNAME") ?: project.findProperty("z2.publish.username"))?.toString()
val publishPassword = (System.getenv("Z2_PUBLISH_PASSWORD") ?: project.findProperty("z2.publish.password"))?.toString()
val isSnapshot = "SNAPSHOT" in project.version.toString()
val isPublishing = project.properties["z2.publish"] != null || System.getenv("Z2_PUBLISH") != null

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

publishing.publications.withType<MavenPublication> {
    val publication = this
    val javadocJar = tasks.register("${publication.name}JavadocJar", Jar::class) {
        archiveClassifier.set("javadoc")
        // Each archive name should be distinct. Mirror the format for the sources Jar tasks.
        archiveBaseName.set("${archiveBaseName.get()}-${publication.name}")
    }
    artifact(javadocJar)
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
        if (project.properties["signing.keyId"] == null) {
            useGpgCmd()
        }
        sign(publishing.publications)
    }

    publishing {

        repositories {
            if (isSnapshot) {
                requireNotNull(publishSnapshotUrl) { throw IllegalStateException("publishing: missing snapshot url, define Z2_PUBLISH_SNAPSHOT_URL") }
                mavenRepo(publishSnapshotUrl)
            } else {
                requireNotNull(publishReleaseUrl) { throw IllegalStateException("publishing: missing release url, define Z2_PUBLISH_RELEASE_URL") }
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