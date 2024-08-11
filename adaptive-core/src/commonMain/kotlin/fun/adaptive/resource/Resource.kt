/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.resource

/**
 * Represents a resource with an ID and a set of resource items.
 *
 * @property id The ID of the resource.
 * @property items The set of resource items associated with the resource.
 */
open class Resource (
    val id: String,
    val items: Set<ResourceItem>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Resource

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    val path : String
        get() = items.single().path

    val uri : String
        get() = defaultResourceReader.getUri(path)

}

data class ResourceItem(
    val qualifiers: Set<Qualifier>,
    val path: String,
    val offset: Long,
    val size: Long,
)