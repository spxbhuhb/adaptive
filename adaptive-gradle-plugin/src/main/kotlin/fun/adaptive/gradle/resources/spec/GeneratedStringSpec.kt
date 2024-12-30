/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.gradle.resources.spec

import com.squareup.kotlinpoet.*
import `fun`.adaptive.gradle.internal.utils.uppercaseFirstChar
import java.nio.file.Path
import java.util.*
import kotlin.io.path.invariantSeparatorsPathString

internal enum class ResourceType(val typeName: String, val accessorName: String) {
    FILE("file", "file"),
    FONT("font", "font"),
    GRAPHICS("graphics", "graphics"),
    IMAGE("image", "image"),
    STRING("string", "string");

    override fun toString(): String = typeName

    companion object {
        fun fromString(str: String): ResourceType? =
            entries.firstOrNull { it.typeName.equals(str, true) }
    }
}

internal data class ResourceItem(
    val type: ResourceType,
    val qualifiers: List<String>,
    val name: String,
    val path: Path,
    val offset: Long = -1,
    val size: Long = -1,
)

private fun ResourceType.getClassName(): ClassName = when (this) {
    ResourceType.FILE -> ClassName("fun.adaptive.resource.file", "FileResource")
    ResourceType.FONT -> ClassName("fun.adaptive.resource.font", "FontResource")
    ResourceType.GRAPHICS -> ClassName("fun.adaptive.resource.graphics", "GraphicsResource")
    ResourceType.IMAGE -> ClassName("fun.adaptive.resource.image", "FileResource")
    ResourceType.STRING -> ClassName("fun.adaptive.resource", "StringResource")
}

private fun ResourceType.requiresKeyName() =
    this in setOf(ResourceType.STRING)

private val resourceItemClass = ClassName("fun.adaptive.resource", "ResourceItem")
private val experimentalAnnotation = AnnotationSpec.builder(
    ClassName("fun.adaptive.resource", "ExperimentalResourceApi")
).build()



// We need to divide accessors by different files because
//
// if all accessors are generated in a single object
// then a build may fail with: org.jetbrains.org.objectweb.asm.MethodTooLargeException: Method too large: Res$drawable.<clinit> ()V
// e.g. https://github.com/JetBrains/compose-multiplatform/issues/4285
//
// if accessor initializers are extracted from the single object but located in the same file
// then a build may fail with: org.jetbrains.org.objectweb.asm.ClassTooLargeException: Class too large: Res$drawable
private const val ITEMS_PER_FILE_LIMIT = 500

internal fun getStringAccessorsSpecs(
    //type -> id -> items
    resources: Map<ResourceType, Map<String, List<ResourceItem>>>,
    packageName: String,
    sourceSetName: String,
    moduleDir: String,
    isPublic: Boolean
): List<FileSpec> {
    val resModifier = if (isPublic) KModifier.PUBLIC else KModifier.INTERNAL
    val files = mutableListOf<FileSpec>()

    //we need to sort it to generate the same code on different platforms
    sortResources(resources).forEach { (type, idToResources) ->
        val chunks = idToResources.keys.chunked(ITEMS_PER_FILE_LIMIT)

        chunks.forEachIndexed { index, ids ->
            files.add(
                getChunkFileSpec(
                    type,
                    "${type.accessorName.uppercaseFirstChar()}$index.$sourceSetName",
                    sourceSetName.uppercaseFirstChar() + type.accessorName.uppercaseFirstChar() + index,
                    packageName,
                    moduleDir,
                    resModifier,
                    idToResources.subMap(ids.first(), true, ids.last(), true)
                )
            )
        }
    }

    return files
}

private fun getChunkFileSpec(
    type: ResourceType,
    fileName: String,
    chunkClassName: String,
    packageName: String,
    moduleDir: String,
    resModifier: KModifier,
    idToResources: Map<String, List<ResourceItem>>
): FileSpec {
    return FileSpec.builder(packageName, fileName).also { chunkFile ->

        val objectSpec = TypeSpec.objectBuilder(chunkClassName).also { typeObject ->
            typeObject.addModifiers(KModifier.PRIVATE)
            val properties = idToResources.keys.map { resName ->
                PropertySpec.builder(resName, type.getClassName())
                    .delegate("\nlazy·{ init_$resName() }")
                    .build()
            }
            typeObject.addProperties(properties)
        }.build()
        chunkFile.addType(objectSpec)

        idToResources.forEach { (resName, items) ->
            val accessor = PropertySpec.builder(resName, type.getClassName(), resModifier)
                .receiver(ClassName(packageName, "Res", type.accessorName))
                .getter(FunSpec.getterBuilder().addStatement("return $chunkClassName.$resName").build())
                .build()
            chunkFile.addProperty(accessor)

            val initializer = FunSpec.builder("init_$resName")
                .addModifiers(KModifier.PRIVATE)
                .returns(type.getClassName())
                .addStatement(
                    CodeBlock.builder()
                        .add("return %T(\n", type.getClassName()).withIndent {
                            add("\"${type}:${resName}\",")
                            if (type.requiresKeyName()) add(" \"$resName\",")
                            withIndent {
                                add("\nsetOf(\n").withIndent {
                                    items.forEach { item ->
                                        add("%T(", resourceItemClass)
                                        add("setOf(").addQualifiers(item.qualifiers).add("), ")
                                        //file separator should be '/' on all platforms
                                        add("\"$moduleDir${item.path.invariantSeparatorsPathString}\", ")
                                        add("${item.offset}, ${item.size}")
                                        add("),\n")
                                    }
                                }
                                add(")\n")
                            }
                        }
                        .add(")")
                        .build().toString()
                )
                .build()
            chunkFile.addFunction(initializer)
        }
    }.build()
}

private fun sortResources(
    resources: Map<ResourceType, Map<String, List<ResourceItem>>>
): TreeMap<ResourceType, TreeMap<String, List<ResourceItem>>> {
    val result = TreeMap<ResourceType, TreeMap<String, List<ResourceItem>>>()
    resources
        .entries
        .forEach { (type, items) ->
            val typeResult = TreeMap<String, List<ResourceItem>>()
            items
                .entries
                .forEach { (name, resItems) ->
                    typeResult[name] = resItems.sortedBy { it.path }
                }
            result[type] = typeResult
        }
    return result
}