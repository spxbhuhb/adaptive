/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.gradle.resources

import `fun`.adaptive.gradle.ADAPTIVE_TASK_GROUP
import `fun`.adaptive.gradle.internal.IDEA_IMPORT_TASK_NAME
import `fun`.adaptive.gradle.internal.IdeaImportTask
import `fun`.adaptive.resource.codegen.ResourceCompilation
import `fun`.adaptive.utility.DangerousApi
import `fun`.adaptive.utility.uppercaseFirstChar
import kotlinx.io.files.Path
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

internal abstract class ProcessResourcesTask : IdeaImportTask() {

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val publicAccessors: Property<Boolean>

    @get:Input
    abstract val withFileQualifiers: Property<Boolean>

    @get:Input
    abstract val withFileDefault: Property<Boolean>

    @get:Input
    abstract val sourceSetName: Property<String>

    @get:InputFiles
    abstract val originalResourcesPath: Property<File>

    @get:OutputDirectory
    abstract val codeDir: DirectoryProperty

    @get:OutputDirectory
    abstract val preparedDir: DirectoryProperty

    @OptIn(DangerousApi::class) // codeDir and resourcesDir is confined to layout.buildDirectory
    override fun safeAction() {
        if (!originalResourcesPath.get().exists()) return

        ResourceCompilation(
            originalResourcesPath = Path(originalResourcesPath.get().toString()),
            packageName = packageName.get(),
            kmpSourceSet = sourceSetName.get(),
            generatedCodePath = Path(codeDir.get().toString()),
            preparedResourcesPath = Path(preparedDir.get().toString()),
            withFileQualifiers = withFileQualifiers.get(),
            withFileDefault = withFileDefault.get()
        ).compile()
    }

    companion object {

        /**
         * Configure the `adaptiveResources<source-set-name>` task for the given source set.
         * Should be called only when [originalResourcesDir] actually exists.
         */
        fun Project.configureProcessResourcesTask(
            sourceSet: KotlinSourceSet,
            config: Provider<ResourcesExtension>
        ) {
            val packageName = config.getResourcePackage(project)
            val publicAccessors = config.map { it.publicAccessors }
            val withFileQualifiers = config.map { it.withFileQualifiers }
            val withFileDefault = config.map { it.withFileDefault }

            logger.info("Configure resources task for ${sourceSet.name}")

            val genTask = tasks.register(
                sourceSet.getProcessResourcesTaskName(),
                ProcessResourcesTask::class.java
            ) { task ->
                task.group = ADAPTIVE_TASK_GROUP
                task.originalResourcesPath.set(sourceSet.originalResourcesDir())
                task.packageName.set(packageName)
                task.publicAccessors.set(publicAccessors)
                task.withFileQualifiers.set(withFileQualifiers)
                task.withFileDefault.set(withFileDefault)
                task.sourceSetName.set(sourceSet.name)
                task.preparedDir.set(layout.buildDirectory.dir("$RES_GEN_DIR/prepared/${sourceSet.name}Resources"))
                task.codeDir.set(layout.buildDirectory.dir("$RES_GEN_DIR/kotlin/${sourceSet.name}Resources"))
            }

            //register generated source set
            sourceSet.kotlin.srcDir(genTask.map { it.codeDir })

            //setup task execution during IDE import
            tasks.configureEach { importTask ->
                if (importTask.name == IDEA_IMPORT_TASK_NAME) {
                    importTask.dependsOn(tasks.withType(IdeaImportTask::class.java))
                }
            }
        }

        fun Project.getPreparedAdaptiveResourcesDir(sourceSet: KotlinSourceSet): Provider<File> = tasks
            .named(
                sourceSet.getProcessResourcesTaskName(),
                ProcessResourcesTask::class.java
            )
            .flatMap { it.preparedDir.asFile }

        fun KotlinSourceSet.getProcessResourcesTaskName() =
            "processAdaptiveResources${name.uppercaseFirstChar()}"

        fun KotlinSourceSet.originalResourcesDir() =
            project.projectDir.resolve("src/${name}/$ADAPTIVE_RESOURCES_DIR")

    }

}