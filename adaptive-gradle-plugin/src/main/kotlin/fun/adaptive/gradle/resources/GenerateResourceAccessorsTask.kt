/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.gradle.resources

import `fun`.adaptive.gradle.internal.IdeaImportTask
import `fun`.adaptive.gradle.resources.spec.ResourceItem
import `fun`.adaptive.gradle.resources.spec.ResourceType
import `fun`.adaptive.gradle.resources.spec.getAccessorsSpecs
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.File
import java.nio.file.Path
import kotlin.io.path.relativeTo

internal abstract class GenerateResourceAccessorsTask : IdeaImportTask() {

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val publicAccessors: Property<Boolean>

    @get:Input
    abstract val sourceSetName: Property<String>

    @get:Input
    @get:Optional
    abstract val packagingDir: Property<File>

    @get:InputFiles
    @get:SkipWhenEmpty
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val resDir: Property<File>

    @get:OutputDirectory
    abstract val codeDir: DirectoryProperty

    override fun safeAction() {
        val kotlinDir = codeDir.get().asFile
        val rootResDir = resDir.get()
        val sourceSet = sourceSetName.get()

        logger.info("Generate accessors for $rootResDir")

    }
}