package `fun`.adaptive.gradle.project

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.jvm.java
import kotlin.text.set

fun Project.configureKmp() {
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        afterEvaluate {
            val kotlinMppExtension = project.extensions.getByType(KotlinMultiplatformExtension::class.java)

            //kotlinMppExtension.jvmToolchain(11)

            kotlinMppExtension.targets.configureEach { target ->
                target.compilations.configureEach { compilation ->
                    compilation.compileTaskProvider.configure { taskProvider ->
                        taskProvider.compilerOptions {
                            optIn.add("kotlin.time.ExperimentalTime")
                        }
                    }
                }
            }

            kotlinMppExtension.sourceSets.configureEach { sourceSet ->
                sourceSet.languageSettings {
                    languageVersion = "2.0"
                }
            }
        }
    }
}