/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.grapics.svg.parse

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.lib.grapics.svg.*
import hu.simplexion.adaptive.wireformat.xml.XmlAttribute
import hu.simplexion.adaptive.wireformat.xml.XmlElement
import hu.simplexion.adaptive.wireformat.xml.XmlNode
import hu.simplexion.adaptive.wireformat.xml.parseXml
import kotlinx.datetime.Instant

interface SvgInstruction : AdaptiveInstruction

class SvgElement(
    val name: String,
    val instructions: List<SvgInstruction>,
    val children: List<SvgElement>
)

fun parseSvg(source: String): SvgElement {
    val xmlRoot = parseXml(source, skipBlankContent = true)
    requireNotNull(xmlRoot) { "could not parse XML: $source" }
    return toSvg(xmlRoot) !!
}

private fun toSvg(xmlNode: XmlNode): SvgElement? {
    if (xmlNode !is XmlElement) return null

    val instructions = mutableListOf<SvgInstruction>()
    xmlNode.attributes.forEach { toSvg(it, instructions) }

    return SvgElement(
        xmlNode.tag,
        instructions,
        xmlNode.children.mapNotNull { toSvg(it) }
    )
}

private fun toSvg(xmlAttribute: XmlAttribute, instructions: MutableList<SvgInstruction>) {
    val value = xmlAttribute.value

    when (xmlAttribute.name) {
        "transform" -> transform(value, instructions)
        "fill" -> fill(value, instructions)
        "d" -> d(value, instructions)
        "height" -> height(value, instructions)
        "width" -> width(value, instructions)
        "viewBox" -> viewBox(value, instructions)
        "id" -> Unit
        "xmlns" -> Unit
        else -> throw IllegalArgumentException("Unknown attribute ${xmlAttribute.name}")
    }
}

private fun transform(value: String, instructions: MutableList<SvgInstruction>) {
    instructions += parseTransform(value)
}

private fun fill(value: String, instructions: MutableList<SvgInstruction>) {
    instructions += Fill(value)
}

private fun d(value: String, instructions: MutableList<SvgInstruction>) {
    instructions += D(parsePath(value))
}

private fun height(value: String, instructions: MutableList<SvgInstruction>) {
    instructions +=Height(value)
}

private fun width(value: String, instructions: MutableList<SvgInstruction>) {
    instructions += Width(value)
}

private fun viewBox(value: String, instructions: MutableList<SvgInstruction>) {
    val params = value.toFloats()
    require(params.size == 4) { "invalid viewBox parameter number ${params.size} (should be 4)" }
    instructions += ViewBox(params[0], params[1], params[2], params[3])
}



