package `fun`.adaptive.gradle.project

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

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
        }
    }
}