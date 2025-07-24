package `fun`.adaptive.internal.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class InternalGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            //pluginManager.apply("com.android.application") // Example for Android apps

            // You can also define common tasks, extensions, etc.
//            tasks.register("myCustomAppTask") {
//                doLast {
//                    println("Running custom app task for project: ${project.name}")
//                }
//            }

            plugins.withId("org.jetbrains.kotlin.multiplatform") {
                afterEvaluate {
                    val kotlinMppExtension = project.extensions.getByType<KotlinMultiplatformExtension>()
                    kotlinMppExtension.sourceSets.forEach { sourceSet ->
                        sourceSet.languageSettings.languageVersion = "2.1"
                        sourceSet.languageSettings.apiVersion = "2.1"
                    }
                }
            }
        }
    }
}