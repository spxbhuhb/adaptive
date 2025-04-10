/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.gradle.internal

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

internal const val IDEA_IMPORT_TASK_NAME = "prepareKotlinIdeaImport"

internal fun Project.ideaIsInSyncProvider(): Provider<Boolean> = provider {
    System.getProperty("idea.sync.active", "false").toBoolean()
}

/**
 * This task should be FAST and SAFE! Because it is being run during IDE import.
 */
internal abstract class IdeaImportTask : DefaultTask() {
    @get:Input
    val ideaIsInSync: Provider<Boolean> = project.ideaIsInSyncProvider()

    @TaskAction
    fun run() {
        try {
            safeAction()
        } catch (e: Exception) {
            //message must contain two ':' symbols to be parsed by IDE UI!
            logger.error("e: $name task was failed:", e)
            if (!ideaIsInSync.get()) throw e
        }
    }

    abstract fun safeAction()
}