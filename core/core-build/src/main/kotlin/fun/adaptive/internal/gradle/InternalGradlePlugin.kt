package `fun`.adaptive.internal.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

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
        }
    }
}