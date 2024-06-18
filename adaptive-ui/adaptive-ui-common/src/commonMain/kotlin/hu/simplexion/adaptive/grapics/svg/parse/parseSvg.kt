/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.parse

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.grapics.svg.SvgAdapter
import hu.simplexion.adaptive.grapics.svg.SvgFragment
import hu.simplexion.adaptive.grapics.svg.fragment.SvgGroup
import hu.simplexion.adaptive.grapics.svg.fragment.SvgPath
import hu.simplexion.adaptive.grapics.svg.fragment.SvgRoot
import hu.simplexion.adaptive.grapics.svg.instruction.*
import hu.simplexion.adaptive.wireformat.xml.XmlAttribute
import hu.simplexion.adaptive.wireformat.xml.XmlElement
import hu.simplexion.adaptive.wireformat.xml.parseXml

interface SvgInstruction : AdaptiveInstruction

fun parseSvg(adapter : SvgAdapter, source: String): SvgFragment<*> {
    val xmlRoot = parseXml(source, skipBlankContent = true)
    requireNotNull(xmlRoot) { "could not parse XML: $source" }
    return toSvg(xmlRoot, adapter, null)
}

private fun toSvg(xmlElement: XmlElement, adapter : SvgAdapter, parent : SvgFragment<*>?): SvgFragment<*> {

    val instructions = mutableListOf<SvgInstruction>()
    xmlElement.attributes.forEach { toSvg(it, instructions) }

    val fragment = when (xmlElement.tag) {
        "svg" -> SvgRoot(adapter, parent, -1)
        "g" -> SvgGroup(adapter, parent, -1)
        "path" -> SvgPath(adapter, parent, -1)
        else -> throw NotImplementedError("svg type ${xmlElement.tag} is not implemented yet")
    }

    fragment.state[fragment.instructionIndex] = instructions.toTypedArray()
    // no external patch for SVG fragments when loaded directly
    fragment.genPatchInternal()

    for (child in xmlElement.children) {
        if (child !is XmlElement) continue
        fragment.children += toSvg(child, adapter, fragment)
    }

    return fragment
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
    instructions += Height(value)
}

private fun width(value: String, instructions: MutableList<SvgInstruction>) {
    instructions += Width(value)
}

private fun viewBox(value: String, instructions: MutableList<SvgInstruction>) {
    val params = value.toDoubles()
    require(params.size == 4) { "invalid viewBox parameter number ${params.size} (should be 4)" }
    instructions += ViewBox(params[0], params[1], params[2], params[3])
}



