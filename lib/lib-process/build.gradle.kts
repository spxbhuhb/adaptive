import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    signing
    alias(libs.plugins.gradleMavenPublish)
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()

val baseName = "lib-process"
val pomName = "Adaptive Lib Process"
val scmPath = "spxbhuhb/adaptive"

// this is ugly but I don't use JS dependencies anyway, 
// https://youtrack.jetbrains.com/issue/KT-50848/Kotlin-JS-inner-build-routines-are-using-vulnerable-NPM-dependencies-and-now-that-we-have-kotlin-js-store-github-audit-this
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().lockFileName = "skip-yarn-lock"
}

kotlin {

    jvmToolchain(11)

    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

    js(IR) {
        browser()
        binaries.library()
    }

    if (libs.versions.ios.support.get() != "none") {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        )
    }

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.adaptive.core.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

mavenPublishing {

    publishToMavenCentral()

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