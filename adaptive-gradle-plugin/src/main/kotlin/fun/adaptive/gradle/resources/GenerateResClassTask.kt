/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.gradle.resources

import `fun`.adaptive.gradle.resources.utils.IdeaImportTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import java.io.File

internal abstract class GenerateResClassTask : IdeaImportTask() {
    companion object {
        private const val RES_FILE_NAME = "Res"
    }

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    @get:Optional
    abstract val packagingDir: Property<File>

    @get:Input
    abstract val shouldGenerateCode: Property<Boolean>

    @get:Input
    abstract val makeAccessorsPublic: Property<Boolean>

    @get:OutputDirectory
    abstract val codeDir: DirectoryProperty

    override fun safeAction() {
        val dir = codeDir.get().asFile
        dir.deleteRecursively()
        dir.mkdirs()

        if (shouldGenerateCode.get()) {
            logger.info("Generate $RES_FILE_NAME.kt")

            val pkgName = packageName.get()
            val moduleDirectory = packagingDir.getOrNull()?.let { it.invariantSeparatorsPath + "/" } ?: ""
            val isPublic = makeAccessorsPublic.get()
            getResFileSpec(pkgName, RES_FILE_NAME, moduleDirectory, isPublic).writeTo(dir)
        } else {
            logger.info("Generation Res class is disabled")
        }
    }
}