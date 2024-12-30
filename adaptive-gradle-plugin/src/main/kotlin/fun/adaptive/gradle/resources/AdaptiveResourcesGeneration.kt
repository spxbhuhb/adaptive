/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.gradle.resources

import `fun`.adaptive.gradle.internal.utils.IDEA_IMPORT_TASK_NAME
import `fun`.adaptive.gradle.internal.IdeaImportTask
import `fun`.adaptive.gradle.internal.utils.uppercaseFirstChar
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

internal fun Project.configureAdaptiveResourcesGeneration(
    kotlinExtension: KotlinProjectExtension,
    config: Provider<ResourcesExtension>,
    generateModulePath: Boolean
) {
    logger.info("Configure Adaptive resources generation")

    val packageName = config.getResourcePackage(project)
    val publicAccessors = config.map { it.publicAccessors }
    val packagingDir = config.getModuleResourcesDir(project)

    kotlinExtension.sourceSets.all { sourceSet ->

        //common resources must be converted (XML -> AVS)
        val preparedResourcesTask = registerPrepareAdaptiveResourcesTask(sourceSet)
        val preparedResources = preparedResourcesTask.flatMap { it.outputDir.asFile }

        configureResourceAccessorsGeneration(
            sourceSet,
            publicAccessors,
            preparedResources,
            packageName,
            packagingDir,
            generateModulePath
        )
    }

    //setup task execution during IDE import
    tasks.configureEach { importTask ->
        if (importTask.name == IDEA_IMPORT_TASK_NAME) {
            importTask.dependsOn(tasks.withType(IdeaImportTask::class.java))
        }
    }
}

private fun Project.configureResourceAccessorsGeneration(
    sourceSet: KotlinSourceSet,
    publicAccessors: Provider<Boolean>,
    resourcesDir: Provider<File>,
    packageName: Provider<String>,
    packagingDir: Provider<File>,
    generateModulePath: Boolean
) {
    logger.info("Configure resource accessors generation for ${sourceSet.name}")

    val genTask = tasks.register(
        "generateResourceAccessorsFor${sourceSet.name.uppercaseFirstChar()}",
        GenerateResourceAccessorsTask::class.java
    ) { task ->
        task.packageName.set(packageName)
        task.publicAccessors.set(publicAccessors)
        task.sourceSetName.set(sourceSet.name)
        task.resDir.set(resourcesDir)
        task.codeDir.set(layout.buildDirectory.dir("$RES_GEN_DIR/kotlin/${sourceSet.name}Resources"))

        if (generateModulePath) {
            task.packagingDir.set(packagingDir)
        }
    }

    //register generated source set
    sourceSet.kotlin.srcDir(genTask.map { it.codeDir })
}