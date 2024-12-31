/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.gradle.resources

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.lint.AndroidLintAnalysisTask
import com.android.build.gradle.internal.lint.LintModelWriterTask
import `fun`.adaptive.gradle.internal.registerTask
import `fun`.adaptive.gradle.resources.ProcessResourcesTask.Companion.getPreparedAdaptiveResourcesDir
import `fun`.adaptive.utility.uppercaseFirstChar
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmAndroidCompilation
import org.jetbrains.kotlin.gradle.plugin.sources.android.androidSourceSetInfoOrNull
import javax.inject.Inject

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal fun Project.configureAndroidAdaptiveResources(
    kotlinExtension: KotlinMultiplatformExtension,
    androidExtension: BaseExtension
) {
    // 1) get the Kotlin Android Target Compilation -> [A]
    // 2) get default source set name for the 'A'
    // 3) find the associated Android SourceSet in the AndroidExtension -> [B]
    // 4) get all source sets in the 'A' and add its resources to the 'B'
    kotlinExtension.targets.withType(KotlinAndroidTarget::class.java).all { androidTarget ->
        androidTarget.compilations.all { compilation: KotlinJvmAndroidCompilation ->
            compilation.defaultSourceSet.androidSourceSetInfoOrNull?.let { kotlinAndroidSourceSet ->
                androidExtension.sourceSets
                    .matching { it.name == kotlinAndroidSourceSet.androidSourceSetName }
                    .all { androidSourceSet ->
                        compilation.allKotlinSourceSets.forAll { kotlinSourceSet ->
                            val preparedAdaptiveResources = getPreparedAdaptiveResourcesDir(kotlinSourceSet)
                            androidSourceSet.resources.srcDirs(preparedAdaptiveResources)

                            //fix for AGP < 8.0
                            //usually 'androidSourceSet.resources.srcDir(preparedCommonResources)' should be enough
                            compilation.androidVariant.processJavaResourcesProvider.configure {
                                it.dependsOn(preparedAdaptiveResources)
                            }
                        }
                    }
            }
        }
    }

    //copy fonts from the compose resources dir to android assets
    val androidComponents = project.extensions.findByType(AndroidComponentsExtension::class.java) ?: return
    androidComponents.onVariants { variant ->
        val variantResources = project.files()

        kotlinExtension.targets.withType(KotlinAndroidTarget::class.java).all { androidTarget ->
            androidTarget.compilations.all { compilation: KotlinJvmAndroidCompilation ->
                if (compilation.androidVariant.name == variant.name) {
                    project.logger.info("Configure fonts for variant ${variant.name}")
                    compilation.allKotlinSourceSets.forAll { kotlinSourceSet ->
                        val preparedAdaptiveResources = getPreparedAdaptiveResourcesDir(kotlinSourceSet)
                        variantResources.from(preparedAdaptiveResources)
                    }
                }
            }
        }

        val copyFonts = registerTask<CopyAndroidFontsToAssetsTask>(
            "copy${variant.name.uppercaseFirstChar()}FontsToAndroidAssets"
        ) {
            from.set(variantResources)
        }
        @Suppress("UnstableApiUsage")
        variant.sources.assets?.addGeneratedSourceDirectory(
            taskProvider = copyFonts,
            wiredWith = CopyAndroidFontsToAssetsTask::outputDirectory
        )
        //exclude a duplication of fonts in apks
        variant.packaging.resources.excludes.add("**/font*/*")
    }
}

//Copy task doesn't work with 'variant.sources?.assets?.addGeneratedSourceDirectory' API
internal abstract class CopyAndroidFontsToAssetsTask : DefaultTask() {
    @get:Inject
    abstract val fileSystem: FileSystemOperations

    @get:InputFiles
    @get:IgnoreEmptyDirectories
    abstract val from: Property<FileCollection>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun action() {
        fileSystem.copy {
            it.includeEmptyDirs = false
            it.from(from)
            it.include("**/font*/*")
            it.into(outputDirectory)
        }
    }
}

/*
  There is a dirty fix for the problem:

  Reason: Task ':generateDemoDebugUnitTestLintModel' uses this output of task ':generateResourceAccessorsForAndroidUnitTest' without declaring an explicit or implicit dependency. This can lead to incorrect results being produced, depending on what order the tasks are executed.

  Possible solutions:
    1. Declare task ':generateResourceAccessorsForAndroidUnitTest' as an input of ':generateDemoDebugUnitTestLintModel'.
    2. Declare an explicit dependency on ':generateResourceAccessorsForAndroidUnitTest' from ':generateDemoDebugUnitTestLintModel' using Task#dependsOn.
    3. Declare an explicit dependency on ':generateResourceAccessorsForAndroidUnitTest' from ':generateDemoDebugUnitTestLintModel' using Task#mustRunAfter.
 */
internal fun Project.fixAndroidLintTaskDependencies() {
    tasks.matching {
        it is AndroidLintAnalysisTask || it is LintModelWriterTask
    }.configureEach {
        it.mustRunAfter(tasks.withType(ProcessResourcesTask::class.java))
    }
}