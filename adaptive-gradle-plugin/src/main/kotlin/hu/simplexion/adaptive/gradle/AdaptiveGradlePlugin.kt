/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import java.io.File

const val PLUGIN_VERSION = "2024.05.15-SNAPSHOT"
const val KOTLIN_COMPILER_PLUGIN_ID = "adaptive"  // this is how the compiler identifies the plugin
const val KOTLIN_COMPILER_PLUGIN_ARTIFACT_GROUP = "hu.simplexion.adaptive"
const val KOTLIN_COMPILER_PLUGIN_ARTIFACT_NAME = "adaptive-kotlin-plugin"
const val GRADLE_EXTENSION_NAME = "adaptive"

@Suppress("unused")
class AdaptiveGradlePlugin : KotlinCompilerPluginSupportPlugin {

    companion object {
        @JvmStatic
        fun getOutputDir(project: Project, sourceSetName: String, target: String) =
            File(project.project.layout.buildDirectory.get().asFile, "generated/adaptive/$target/$sourceSetName")

        @JvmStatic
        fun getResourceOutputDir(project: Project, sourceSetName: String, target: String) =
            File(getOutputDir(project, sourceSetName, target), "resources")
    }

    override fun apply(target: Project): Unit = with(target) {
        extensions.create(GRADLE_EXTENSION_NAME, AdaptiveGradleExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation.target.project.plugins.hasPlugin(AdaptiveGradlePlugin::class.java)

    override fun getCompilerPluginId(): String = KOTLIN_COMPILER_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = KOTLIN_COMPILER_PLUGIN_ARTIFACT_GROUP,
        artifactId = KOTLIN_COMPILER_PLUGIN_ARTIFACT_NAME,
        version = PLUGIN_VERSION
    )

    override fun getPluginArtifactForNative(): SubpluginArtifact = SubpluginArtifact(
        groupId = KOTLIN_COMPILER_PLUGIN_ARTIFACT_GROUP,
        artifactId = "$KOTLIN_COMPILER_PLUGIN_ARTIFACT_NAME-native",
        version = PLUGIN_VERSION
    )

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project

        // This does not work for multiplatform projects. So I'll go with manual dependency add for now.
        // TODO check automatic dependency addition from gradle plugin
//        kotlinCompilation.dependencies {
//            implementation("$PLUGIN_GROUP:$RUNTIME_NAME:$PLUGIN_VERSION")
//        }

        val extension = project.extensions.getByType(AdaptiveGradleExtension::class.java)

//        val target = kotlinCompilation.target.name
//        val sourceSetName = kotlinCompilation.defaultSourceSet.name

        val options = mutableListOf<SubpluginOption>()

        options += SubpluginOption(key = "resource-dir", extension.resourceDir.get().toString())
        options += SubpluginOption(key = "plugin-debug", extension.pluginDebug.get().toString())

        extension.pluginLogDir.get().let {
            options += SubpluginOption(key = "plugin-log-dir", it.toString())
        }

        return project.provider { options }
    }
}
