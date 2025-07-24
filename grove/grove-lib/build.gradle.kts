import `fun`.adaptive.internal.gradle.setupPublishing
import `fun`.adaptive.internal.gradle.skipYarnLock

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.adaptive)
    signing
    alias(libs.plugins.gradleMavenPublish)
    id("fun.adaptive.internal.gradle")
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()
description = "Adaptive Grove Library"

skipYarnLock()

adaptive {
    resources {
        publicAccessors = true
        packageOfResources = "fun.adaptive.grove.generated.resources"
    }
}

kotlin {

    jvmToolchain(11)

    jvm()

    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.adaptive.core.core)
                implementation(libs.adaptive.core.ui)

                implementation(libs.adaptive.lib.graphics)
                implementation(libs.adaptive.lib.ui)
                implementation(libs.adaptive.lib.ui.mpw)

                implementation(libs.adaptive.grove.runtime)
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

setupPublishing()