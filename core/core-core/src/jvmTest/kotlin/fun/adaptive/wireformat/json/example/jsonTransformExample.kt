package `fun`.adaptive.wireformat.json.example

import `fun`.adaptive.persistence.readJson
import `fun`.adaptive.persistence.writeJson
import kotlinx.io.files.Path

fun main(argv: Array<String>) {

    val path = Path(argv[0])

    val original = path.readJson() ?: throw IllegalArgumentException("File ${path.name} is empty")

    val transformed = original.accept(ExampleJsonTransformer(), null)

    path.writeJson(transformed, overwrite = true)

}