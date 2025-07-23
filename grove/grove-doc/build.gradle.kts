/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:OptIn(ExperimentalWasmDsl::class)

import com.vanniktech.maven.publish.SonatypeHost
import `fun`.adaptive.foundation.testing.test
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    signing
    alias(libs.plugins.gradleMavenPublish)
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()

val baseName = "grove-doc"
val pomName = "Adaptive Grove Documenation Builder"
val scmPath = "spxbhuhb/adaptive"

// this is ugly but I don't use JS dependencies anyway, 
// https://youtrack.jetbrains.com/issue/KT-50848/Kotlin-JS-inner-build-routines-are-using-vulnerable-NPM-dependencies-and-now-that-we-have-kotlin-js-store-github-audit-this
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().lockFileName = "skip-yarn-lock"
}

adaptive {
    resources {
        publicAccessors = true
        packageOfResources = "fun.adaptive.grove.doc.generated.resources"
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    jvmToolchain(11)

    jvm()

    js(IR) {
        browser {
            testTask {
                enabled = false
            }
        }
        binaries.library()
    }

    wasmJs {
        browser {
            testTask {
                enabled = false
            }
        }
        binaries.library()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.adaptive.core.core)
                implementation(libs.adaptive.core.ui)

                implementation(libs.adaptive.lib.auth)
                implementation(libs.adaptive.lib.document)
                implementation(libs.adaptive.lib.ui)
                implementation(libs.adaptive.lib.ui.mpw)
                implementation(libs.adaptive.lib.util)
                implementation(libs.adaptive.lib.value)
            }
        }

        commonTest {
            dependencies {
                api(libs.kotlin.test)
            }
        }
    }

}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}

tasks.create<JavaExec>("compileAdaptiveDocumentation") {
    classpath = sourceSets["jvmMain"].runtimeClasspath
    mainClass.set("fun.adaptive.grove.doc.MainKt")

    val input = project.rootDir.resolve("../..") // this points to the root directory of the repo
    val mdOutput = input.resolve("build/adaptive/doc")
    val valuesOutput = input.resolve("site/site-app/var/values")

    args(input, mdOutput, valuesOutput)

    doFirst {
        mdOutput.deleteRecursively()
        valuesOutput.deleteRecursively()
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

mavenPublishing {

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates("fun.adaptive", baseName, version.toString())

    pom {
        description.set(project.name)
        name.set(pomName)
        url.set("https://adaptive.fun")
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