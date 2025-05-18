package `fun`.adaptive.resource.codegen

import `fun`.adaptive.code.kotlin.writer.kotlinWriter
import `fun`.adaptive.code.kotlin.writer.kwFile
import `fun`.adaptive.code.kotlin.writer.kwModule
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.resource.codegen.kotlin.unstructuredResource
import `fun`.adaptive.persistence.resolve
import `fun`.adaptive.persistence.write

/**
 * We need to divide accessors by different files because if all accessors are
 * generated in a single object then a build may fail with:
 *
 * org.jetbrains.org.objectweb.asm.MethodTooLargeException: Method too large: Res$drawable.<clinit> ()V
 *
 * e.g. https://github.com/JetBrains/compose-multiplatform/issues/4285
 *
 * If accessor initializers are extracted from the single object, but located in
 * the same file then a build may fail with:
 *
 * org.jetbrains.org.objectweb.asm.ClassTooLargeException: Class too large: Res$drawable
 */
fun ResourceCompilation.generateUnstructuredAccessors(resourceType: ResourceTypeQualifier) {

    val chunks = resourceSetsByType[resourceType] !!.values.chunked(500)

    logger?.apply {
        fine { "Generating resource accessors for $resourceType" }
        resourceSetsByType[resourceType] !!.values.forEach { resourceSet ->
            resourceSet.files.forEach {
                fine { "    Resource set: ${resourceSet.name}" }
                fine { "        ${it.path}" }
            }
        }
    }

    kotlinWriter {
        kwModule {
            chunks.forEachIndexed { index, chunk ->
                val accessorObjectName = "$kmpSourceSet${resourceType.name}${index}"

                kwFile(
                    fileName = accessorObjectName,
                    packageName = packageName
                ) {
                    unstructuredResource(accessorObjectName, chunk)
                }
            }
        }
    }.also { writer ->
        writer.render()
        writer.modules.first().files.forEach { file ->
            generatedCodePath.resolve(file.name + ".kt").write(file.renderedSource)
        }
    }

}