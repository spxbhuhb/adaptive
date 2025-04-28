/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.parse

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.graphics.canvas.instruction.Fill
import `fun`.adaptive.graphics.svg.SvgAdapter
import `fun`.adaptive.graphics.svg.SvgFragment
import `fun`.adaptive.graphics.svg.fragment.SvgGroup
import `fun`.adaptive.graphics.svg.fragment.SvgPath
import `fun`.adaptive.graphics.svg.fragment.SvgRoot
import `fun`.adaptive.graphics.svg.instruction.D
import `fun`.adaptive.graphics.svg.instruction.SvgHeight
import `fun`.adaptive.graphics.svg.instruction.SvgWidth
import `fun`.adaptive.graphics.svg.instruction.ViewBox
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.wireformat.xml.XmlAttribute
import `fun`.adaptive.wireformat.xml.XmlElement
import `fun`.adaptive.wireformat.xml.parseXml

fun parseSvg(
    adapter: SvgAdapter,
    source: String,
    additionalInstructions: Array<out AdaptiveInstruction> = emptyArray()
): SvgFragment<*> {
    val xmlRoot = parseXml(source, skipBlankContent = true)
    requireNotNull(xmlRoot) { "could not parse XML: $source" }
    return toSvg(xmlRoot, adapter, null, additionalInstructions)
}

private fun toSvg(
    xmlElement: XmlElement,
    adapter: SvgAdapter,
    parent: SvgFragment<*>?,
    additionalInstructions: Array<out AdaptiveInstruction>
): SvgFragment<*> {

    val instructions = mutableListOf<AdaptiveInstruction>()
    xmlElement.attributes.forEach { toSvg(it, instructions) }

    val fragment = when (xmlElement.tag) {
        "svg" -> SvgRoot(adapter, parent, -1)
        "g" -> SvgGroup(adapter, parent, -1)
        "path" -> SvgPath(adapter, parent, -1)
        else -> throw NotImplementedError("svg type ${xmlElement.tag} is not implemented yet")
    }

    fragment.set(0, AdaptiveInstructionGroup(instructions.toTypedArray() + additionalInstructions))
    // no external patch for SVG fragments when loaded directly
    fragment.genPatchInternal()

    for (child in xmlElement.children) {
        if (child !is XmlElement) continue
        fragment.children += toSvg(child, adapter, fragment, additionalInstructions)
    }

    return fragment
}

private fun toSvg(xmlAttribute: XmlAttribute, instructions: MutableList<AdaptiveInstruction>) {
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

private fun transform(value: String, instructions: MutableList<AdaptiveInstruction>) {
    instructions += parseTransform(value)
}

private fun fill(value: String, instructions: MutableList<AdaptiveInstruction>) {
    instructions += Fill(Color.decodeFromHex(value))
}

private fun d(value: String, instructions: MutableList<AdaptiveInstruction>) {
    instructions += D(parsePath(value))
}

private fun height(value: String, instructions: MutableList<AdaptiveInstruction>) {
    instructions += SvgHeight(value)
}

private fun width(value: String, instructions: MutableList<AdaptiveInstruction>) {
    instructions += SvgWidth(value)
}

private fun viewBox(value: String, instructions: MutableList<AdaptiveInstruction>) {
    val params = value.toDoubles()
    require(params.size == 4) { "invalid viewBox parameter number ${params.size} (should be 4)" }
    instructions += ViewBox(params[0], params[1], params[2], params[3])
}



