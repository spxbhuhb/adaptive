/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package hu.simplexion.adaptive.gradle.resources

import com.android.build.gradle.BaseExtension
import hu.simplexion.adaptive.gradle.resources.utils.KOTLIN_JVM_PLUGIN_ID
import hu.simplexion.adaptive.gradle.resources.utils.KOTLIN_MPP_PLUGIN_ID
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.extraProperties

internal const val ADAPTIVE_RESOURCES_DIR = "adaptiveResources"
internal const val RES_GEN_DIR = "generated/adaptive/resourceGenerator"
private const val KMP_RES_EXT = "multiplatformResourcesPublication"
private const val MIN_GRADLE_VERSION_FOR_KMP_RESOURCES = "7.6"
private val androidPluginIds = listOf(
    "com.android.application",
    "com.android.library"
)

internal fun Project.configureAdaptiveResources(extension: ResourcesExtension) {
    val config = provider { extension }
    plugins.withId(KOTLIN_MPP_PLUGIN_ID) { onKgpApplied(config, it as KotlinBasePlugin) }
    plugins.withId(KOTLIN_JVM_PLUGIN_ID) { onKotlinJvmApplied(config) }
}

private fun Project.onKgpApplied(config: Provider<ResourcesExtension>, kgp: KotlinBasePlugin) {
    val kotlinExtension = project.extensions.getByType(KotlinMultiplatformExtension::class.java)

    val hasKmpResources = extraProperties.has(KMP_RES_EXT)
    val currentGradleVersion = GradleVersion.current()
    val minGradleVersion = GradleVersion.version(MIN_GRADLE_VERSION_FOR_KMP_RESOURCES)
    val kmpResourcesAreAvailable = hasKmpResources && currentGradleVersion >= minGradleVersion

    if (kmpResourcesAreAvailable) {
        configureKmpResources(kotlinExtension, extraProperties.get(KMP_RES_EXT)!!, config)
        onAgpApplied { fixAndroidLintTaskDependencies() }
    } else {
        val commonMain = KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME
        configureAdaptiveResources(kotlinExtension, commonMain, config)

        onAgpApplied { androidExtension ->
            configureAndroidAdaptiveResources(kotlinExtension, androidExtension)
            fixAndroidLintTaskDependencies()
        }
    }

    configureSyncIosAdaptiveResources(kotlinExtension)
}

private fun Project.onAgpApplied(block: (androidExtension: BaseExtension) -> Unit) {
    androidPluginIds.forEach { pluginId ->
        plugins.withId(pluginId) {
            logger.warn("${project.name} onAgpApplied $pluginId")
            val androidExtension = project.extensions.getByType(BaseExtension::class.java)
            block(androidExtension)
        }
    }
}

private fun Project.onKotlinJvmApplied(config: Provider<ResourcesExtension>) {
    val kotlinExtension = project.extensions.getByType(KotlinProjectExtension::class.java)
    val main = SourceSet.MAIN_SOURCE_SET_NAME
    configureAdaptiveResources(kotlinExtension, main, config)
}

// sourceSet.resources.srcDirs doesn't work for Android targets.
// Android resources should be configured separately
private fun Project.configureAdaptiveResources(
    kotlinExtension: KotlinProjectExtension,
    resClassSourceSetName: String,
    config: Provider<ResourcesExtension>
) {
    logger.info("Configure adaptive resources")
    configureAdaptiveResourcesGeneration(kotlinExtension, resClassSourceSetName, config, false)

    kotlinExtension.sourceSets.all { sourceSet ->
        sourceSet.resources.srcDirs(getPreparedAdaptiveResourcesDir(sourceSet))
    }
}