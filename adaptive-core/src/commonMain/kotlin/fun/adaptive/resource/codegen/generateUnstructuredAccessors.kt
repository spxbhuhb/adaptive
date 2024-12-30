package `fun`.adaptive.resource.codegen

import `fun`.adaptive.kotlin.writer.kotlinWriter
import `fun`.adaptive.kotlin.writer.kwFile
import `fun`.adaptive.kotlin.writer.kwModule
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.resource.codegen.kotlin.unstructuredResource
import `fun`.adaptive.utility.resolve
import `fun`.adaptive.utility.write

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