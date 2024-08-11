/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.gradle.resources

import `fun`.adaptive.gradle.resources.utils.dependsOn
import `fun`.adaptive.gradle.resources.utils.joinLowerCamelCase
import `fun`.adaptive.gradle.resources.utils.registerOrConfigure
import `fun`.adaptive.gradle.resources.utils.uppercaseFirstChar

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Copy
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.konan.target.KonanTarget
import java.io.File

private const val COCOAPODS_PLUGIN_ID = "org.jetbrains.kotlin.native.cocoapods"
private const val IOS_ADAPTIVE_RESOURCES_ROOT_DIR = "adaptive-resources"

internal fun Project.configureSyncIosAdaptiveResources(
    kotlinExtension: KotlinMultiplatformExtension
) {
// I don't know why is this useful, I'll sync all the times probably
//    if (ComposeProperties.dontSyncResources(project).get()) {
//        logger.info(
//            "Compose Multiplatform resource management for iOS is disabled: " +
//                "'${ComposeProperties.SYNC_RESOURCES_PROPERTY}' value is 'false'"
//        )
//        return
//    }

    kotlinExtension.targets.withType(KotlinNativeTarget::class.java).all { nativeTarget ->
        if (nativeTarget.isIosTarget()) {
            nativeTarget.binaries.withType(Framework::class.java).all { iosFramework ->
                val frameworkClassifier = iosFramework.getClassifier()
                val checkNoSandboxTask = tasks.registerOrConfigure<CheckCanAccessAdaptiveResourcesDirectory>(
                    "checkCanSync${frameworkClassifier}AdaptiveResourcesForIos"
                ) {}

                val frameworkResources = files()
                iosFramework.compilation.allKotlinSourceSets.forAll { ss ->
                    frameworkResources.from(ss.resources.sourceDirectories)
                }
                val syncAdaptiveResourcesTask = tasks.registerOrConfigure<SyncAdaptiveResourcesForIosTask>(
                    iosFramework.getSyncResourcesTaskName()
                ) {
                    dependsOn(checkNoSandboxTask)
                    dependsOn(frameworkResources)  //!!! explicit dependency because targetResources is not an input

                    outputDir.set(iosFramework.getFinalResourcesDir())
                    targetResources.put(iosFramework.target.konanTarget.name, frameworkResources)
                }

                val externalTaskName = if (iosFramework.isCocoapodsFramework()) {
                    "syncFramework"
                } else {
                    "embedAndSign${frameworkClassifier}AppleFrameworkForXcode"
                }

                project.tasks.configureEach { task ->
                    if (task.name == externalTaskName) {
                        task.dependsOn(syncAdaptiveResourcesTask)
                    }
                }
            }

            nativeTarget.binaries.withType(TestExecutable::class.java).all { testExec ->
                val copyTestResourcesTask = tasks.registerOrConfigure<Copy>(
                    "copyTestAdaptiveResourcesFor${testExec.target.targetName.uppercaseFirstChar()}"
                ) {
                    from({
                        (testExec.compilation.associatedCompilations + testExec.compilation).flatMap { compilation ->
                            compilation.allKotlinSourceSets.map { it.resources }
                        }
                    })
                    into(testExec.outputDirectory.resolve(IOS_ADAPTIVE_RESOURCES_ROOT_DIR))
                }
                testExec.linkTaskProvider.dependsOn(copyTestResourcesTask)
            }
        }
    }

    plugins.withId(COCOAPODS_PLUGIN_ID) {
        (kotlinExtension as ExtensionAware).extensions.getByType(CocoapodsExtension::class.java).apply {
            framework { podFramework ->
                val syncDir = podFramework.getFinalResourcesDir().get().asFile.relativeTo(projectDir)
                val specAttr = "['${syncDir.path}']"
                extraSpecAttributes["resources"] = specAttr
                project.tasks.named("podInstall").configure {
                    it.doFirst {
                        if (extraSpecAttributes["resources"] != specAttr) error(
                            """
                                |Kotlin.cocoapods.extraSpecAttributes["resources"] is not compatible with Adaptive resources management for iOS.
                                |  * Recommended action: remove extraSpecAttributes["resources"] from '${project.buildFile}' and run '${project.path}:podInstall' once;
                            """.trimMargin()
                            // the properties are not implemented, so I just removed this line
                            // |  * Alternative action: turn off Adaptive's resources management for iOS by adding '${ComposeProperties.SYNC_RESOURCES_PROPERTY}=false' to your gradle.properties;
                        )
                        syncDir.mkdirs()
                    }
                }
            }
        }
    }
}

private fun Framework.getClassifier(): String {
    val suffix = joinLowerCamelCase(buildType.getName(), outputKind.taskNameClassifier)
    return if (name == suffix) ""
    else name.substringBeforeLast(suffix.uppercaseFirstChar()).uppercaseFirstChar()
}

internal fun Framework.getSyncResourcesTaskName() = "sync${getClassifier()}AdaptiveResourcesForIos"
private fun Framework.isCocoapodsFramework() = name.startsWith("pod")

private fun Framework.getFinalResourcesDir(): Provider<Directory> {
    val providers = project.providers
    return if (isCocoapodsFramework()) {
        project.layout.buildDirectory.dir("adaptive/cocoapods/$IOS_ADAPTIVE_RESOURCES_ROOT_DIR/")
    } else {
        providers.environmentVariable("BUILT_PRODUCTS_DIR")
            .zip(
                providers.environmentVariable("CONTENTS_FOLDER_PATH")
            ) { builtProductsDir, contentsFolderPath ->
                File("$builtProductsDir/$contentsFolderPath/$IOS_ADAPTIVE_RESOURCES_ROOT_DIR").canonicalPath
            }
            .flatMap {
                project.objects.directoryProperty().apply { set(File(it)) }
            }
    }
}

private fun KotlinNativeTarget.isIosSimulatorTarget(): Boolean =
    konanTarget === KonanTarget.IOS_X64 || konanTarget === KonanTarget.IOS_SIMULATOR_ARM64

private fun KotlinNativeTarget.isIosDeviceTarget(): Boolean =
    konanTarget === KonanTarget.IOS_ARM64

private fun KotlinNativeTarget.isIosTarget(): Boolean =
    isIosSimulatorTarget() || isIosDeviceTarget()