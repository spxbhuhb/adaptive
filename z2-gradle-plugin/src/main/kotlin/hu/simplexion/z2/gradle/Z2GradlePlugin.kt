/*
 * Copyright (C) 2020 Brian Norman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.simplexion.z2.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import java.io.File

const val PLUGIN_VERSION = "2024.04.19"
const val KOTLIN_COMPILER_PLUGIN_ID = "z2"  // this is how the compiler identifies the plugin
const val KOTLIN_COMPILER_PLUGIN_ARTIFACT_GROUP = "hu.simplexion.z2"
const val KOTLIN_COMPILER_PLUGIN_ARTIFACT_NAME = "z2-kotlin-plugin"
const val GRADLE_EXTENSION_NAME = "z2"

@Suppress("unused")
class Z2GradlePlugin : KotlinCompilerPluginSupportPlugin {

    companion object {
        @JvmStatic
        fun getOutputDir(project: Project, sourceSetName: String, target: String) =
            File(project.project.buildDir, "generated/z2/$target/$sourceSetName")

        @JvmStatic
        fun getResourceOutputDir(project: Project, sourceSetName: String, target: String) =
            File(getOutputDir(project, sourceSetName, target), "resources")
    }

    override fun apply(target: Project): Unit = with(target) {
        extensions.create(GRADLE_EXTENSION_NAME, Z2GradleExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation.target.project.plugins.hasPlugin(Z2GradlePlugin::class.java)

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

        val extension = project.extensions.getByType(Z2GradleExtension::class.java)

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
