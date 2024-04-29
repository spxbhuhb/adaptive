/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.9.10"
    signing
    `maven-publish`
}

group = "hu.simplexion.adaptive"
val scmPath = "spxbhuhb/adaptive"

tasks["clean"].dependsOn(gradle.includedBuild("adaptive-core").task(":clean"))
tasks["build"].dependsOn(gradle.includedBuild("adaptive-core").task(":shadowJar"))

val kotlin_version: String by project.properties

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvmToolchain(11)
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
    "org.jetbrains.kotlin:kotlin-compiler:$kotlin_version".let {
        compileOnly(it)
        testImplementation(it)
    }

    testRuntimeOnly("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-script-runtime:$kotlin_version")
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-annotations-jvm:$kotlin_version")

    testImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    testImplementation("org.jetbrains.kotlin:kotlin-compiler-internal-test-framework:$kotlin_version")
    testImplementation("junit:junit:4.13.2")

    testImplementation(platform("org.junit:junit-bom:5.8.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-commons")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.platform:junit-platform-runner")
    testImplementation("org.junit.platform:junit-platform-suite-api")
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
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        languageVersion = "2.0"
        freeCompilerArgs += "-opt-in=org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi"
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

tasks.create<JavaExec>("generateTests") {
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("hu.simplexion.adaptive.kotlin.GenerateTestsKt")
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

// ---- Publishing -----

val String.propValue
    get() = (System.getenv(this.uppercase().replace('.', '_')) ?: project.findProperty(this))?.toString() ?: ""

val isPublishing = "adaptive.publish".propValue
val publishSnapshotUrl = "adaptive.publish.snapshot.url".propValue
val publishReleaseUrl = "adaptive.publish.release.url".propValue
val publishUsername = "adaptive.publish.username".propValue
val publishPassword = "adaptive.publish.password".propValue
val isSnapshot = "SNAPSHOT" in project.version.toString()

tasks.register("sourcesJar", Jar::class) {
    group = "build"
    description = "Assembles Kotlin sources"

    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
    dependsOn(tasks.classes)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

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

    publications {

        create<MavenPublication>("default") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(javadocJar.get())

            pom {
                description.set("Client-server communication with the absolute minimum amount of boilerplate.")
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
    }
}