
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("hu.simplexion.adaptive")
}

kotlin {

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(projects.shared)
            }
        }
    }
}