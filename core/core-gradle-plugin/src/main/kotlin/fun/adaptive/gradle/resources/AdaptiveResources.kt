/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.gradle.resources

import com.android.build.gradle.BaseExtension
import `fun`.adaptive.gradle.resources.ProcessResourcesTask.Companion.configureProcessResourcesTask
import `fun`.adaptive.gradle.resources.ProcessResourcesTask.Companion.getPreparedAdaptiveResourcesDir
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.extraProperties

internal const val KOTLIN_MPP_PLUGIN_ID = "org.jetbrains.kotlin.multiplatform"

internal const val ADAPTIVE_RESOURCES_DIR = "adaptiveResources"
internal const val RES_GEN_DIR = "generated/adaptive/resource"

private const val KMP_RES_EXT = "multiplatformResourcesPublication"
private const val MIN_GRADLE_VERSION_FOR_KMP_RESOURCES = "7.6"
private val androidPluginIds = listOf(
    "com.android.application",
    "com.android.library"
)

internal fun Project.configureAdaptiveResources(extension: ResourcesExtension) {
    val config = provider { extension }
    plugins.withId(KOTLIN_MPP_PLUGIN_ID) { onKgpApplied(config) }
}

private fun Project.onKgpApplied(config: Provider<ResourcesExtension>) {
    val kotlinExtension = project.extensions.getByType(KotlinMultiplatformExtension::class.java)

    val hasKmpResources = extraProperties.has(KMP_RES_EXT)
    val currentGradleVersion = GradleVersion.current()
    val minGradleVersion = GradleVersion.version(MIN_GRADLE_VERSION_FOR_KMP_RESOURCES)
    val kmpResourcesAreAvailable = hasKmpResources && currentGradleVersion >= minGradleVersion

    if (kmpResourcesAreAvailable) {
        configureKmpResources(kotlinExtension, extraProperties.get(KMP_RES_EXT) !!, config)
        try {
            onAgpApplied { fixAndroidLintTaskDependencies() }
        } catch (_: NoClassDefFoundError) {
            // this means that the android plugin is not there
        }
    } else {
        configureAdaptiveResources(kotlinExtension, config)

        try {
            onAgpApplied { androidExtension ->
                configureAndroidAdaptiveResources(kotlinExtension, androidExtension)
                fixAndroidLintTaskDependencies()
            }
        } catch (_: NoClassDefFoundError) {
            // this means that the android plugin is not there
        }
    }

    configureSyncIosAdaptiveResources(kotlinExtension)
}

private fun Project.onAgpApplied(block: (androidExtension: BaseExtension) -> Unit) {
    androidPluginIds.forEach { pluginId ->
        plugins.withId(pluginId) {
            val androidExtension = project.extensions.getByType(BaseExtension::class.java)
            block(androidExtension)
        }
    }
}

// sourceSet.resources.srcDirs doesn't work for Android targets.
// Android resources should be configured separately
private fun Project.configureAdaptiveResources(
    kotlinExtension: KotlinProjectExtension,
    config: Provider<ResourcesExtension>
) {
    logger.info("Configure adaptive resources")

    kotlinExtension.sourceSets.all { sourceSet ->
        configureProcessResourcesTask(sourceSet, config)
        sourceSet.resources.srcDirs(getPreparedAdaptiveResourcesDir(sourceSet))
    }
}
