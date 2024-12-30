package `fun`.adaptive.resource.codegen

import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.resource.codegen.ResourceCompilation.CompilationReport
import `fun`.adaptive.wireformat.xml.XmlContent
import `fun`.adaptive.wireformat.xml.XmlElement
import `fun`.adaptive.wireformat.xml.parseXml
import kotlinx.io.files.Path

fun ResourceCompilation.collectValuesFromXml(
    path: Path,
    xmlContent: String,
    values: MutableMap<String, ResourceCompilation.ResourceValue>
) {

    val root = try {
        parseXml(xmlContent)
    } catch (e: Exception) {
        compilationError(e) { "Failed to parse XML: $path" }
        return
    }

    if (root == null) {
        compilationError { "Failed to parse XML (root tag is null): $path" }
        return
    }

    if (root.tag != "resources") {
        compilationError { "Failed to parse XML (root tag is not 'resources'): $path" }
        return
    }

    mapValues(values, root, path)
}

fun ResourceCompilation.mapValues(
    values: MutableMap<String, ResourceCompilation.ResourceValue>,
    root: XmlElement,
    path: Path
) {
    for (node in root.children) {

        if (node !is XmlElement) {
            if (node !is XmlContent) {
                compilationError { "Failed to parse XML (unexpected node type): $path" }
                continue
            }
            if (node.content.isNotEmpty()) {
                reports += CompilationReport(LogLevel.Warning, "XML content in unexpected location: $path")
            }
            continue
        }

        mapToValue(node, values, path)
    }
}

fun ResourceCompilation.mapToValue(
    node: XmlElement,
    values: MutableMap<String, ResourceCompilation.ResourceValue>,
    path: Path
) {
    if (node.tag != "string") {
        compilationError { "Failed to parse XML (unexpected tag '${node.tag}'): $path" }
        return
    }

    if (node.attributes.size != 1) {
        compilationError { "Failed to parse XML (unexpected number of attributes for tag 'string'): $path" }
        return
    }

    val attr = node.attributes.first()
    if (attr.name != "name") {
        compilationError { "Failed to parse XML (unexpected attribute name '${attr.name}'): $path" }
        return
    }

    if (attr.value in values) {
        compilationError { "Failed to parse XML (duplicate value '${attr.value}'): $path" }
        return
    }

    if (node.children.size > 1) {
        compilationError { "Failed to parse XML (unexpected number of children for tag 'string'): $path" }
        return
    }

    val value: ByteArray

    if (node.children.isEmpty()) {
        value = ByteArray(0)
    } else {
        val content = node.children.first()
        if (content !is XmlContent) {
            compilationError { "Failed to parse XML (unexpected child node type for tag 'string'): $path" }
            return
        }
        value = content.content.encodeToByteArray()
    }

    values[attr.value] = ResourceCompilation.ResourceValue(
        attr.value,
        value
    )
}
