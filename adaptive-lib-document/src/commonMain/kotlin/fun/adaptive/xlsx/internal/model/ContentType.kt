/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.xlsx.internal.model

import `fun`.adaptive.xlsx.internal.dom.Node

internal class ContentType : Node("Types"), Part {

    override val partName = "/[Content_Types].xml"
    override val contentType = ""
    override val relType = ""

    init {
        this["xmlns"] = "http://schemas.openxmlformats.org/package/2006/content-types"

        addDefault("rels", "application/vnd.openxmlformats-package.relationships+xml")
        addDefault("xml", "application/xml")
    }

    fun addDefault(extension: String, contentType: String) = +node("Default") {
        this["Extension"] = extension
        this["ContentType"] = contentType
    }

    fun addPart(part: Part) = +node("Override") {
        this["PartName"] = part.partName
        this["ContentType"] = part.contentType
    }

}