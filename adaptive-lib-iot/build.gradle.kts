import com.vanniktech.maven.publish.SonatypeHost

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

val baseName = "adaptive-lib-iot"
val pomName = "Adaptive Lib IoT"
val scmPath = "spxbhuhb/adaptive"

// this is ugly but I don't use JS dependencies anyway, 
// https://youtrack.jetbrains.com/issue/KT-50848/Kotlin-JS-inner-build-routines-are-using-vulnerable-NPM-dependencies-and-now-that-we-have-kotlin-js-store-github-audit-this
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().lockFileName = "skip-yarn-lock"
}

kotlin {

    jvmToolchain(11)

    jvm()

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
            implementation(libs.adaptive.core)
            implementation(libs.adaptive.ui)
            implementation(libs.adaptive.lib.ktor)
            implementation(libs.adaptive.lib.auto)
            implementation(libs.adaptive.lib.auth)
            implementation(libs.adaptive.lib.ui)
            implementation(libs.adaptive.lib.graphics)
            implementation(libs.adaptive.lib.document)
            implementation(libs.adaptive.lib.exposed)
            implementation(libs.adaptive.lib.util)
            implementation(libs.adaptive.lib.value)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        jvmTest.dependencies {
            implementation(libs.h2database)
        }
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