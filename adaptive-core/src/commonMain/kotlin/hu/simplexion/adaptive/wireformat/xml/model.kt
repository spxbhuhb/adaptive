/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.xml

interface XmlNode {
    val isElement : Boolean
}

data class XmlContent(
    val content : String
) : XmlNode {
    override val isElement get() = false
}

data class XmlElement(
    val tag : String,
    val attributes : List<XmlAttribute>,
    val children : MutableList<XmlNode>
) : XmlNode {
    override val isElement get() = true
}

data class XmlAttribute(
    val name : String,
    val value : String
)