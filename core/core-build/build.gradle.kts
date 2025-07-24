plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "fun.adaptive"
version = libs.versions.adaptive.get()

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    compileOnly(libs.gradleMavenPublish)
    //implementation("org.jetbrains.dokka:dokka-gradle-plugin:${libs.versions.dokka.get()}")
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        create("adaptiveInternalBuildPlugin") {
            id = "fun.adaptive.internal.gradle"
            implementationClass = "fun.adaptive.internal.gradle.InternalGradlePlugin"
        }
    }
}