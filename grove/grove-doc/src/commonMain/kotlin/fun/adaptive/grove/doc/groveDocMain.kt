package `fun`.adaptive.grove.doc

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.resolve
import kotlinx.io.files.Path

fun main(args: Array<String>) {
    check(args.size == 2) { "usage: <in> <out>" }

    val inPath = Path(args[0])
    val outPath = Path(args[1]).ensure()

    val compilation = GroveDocCompilation(
        inPath,
        outPath
    )

    GroveDocCompiler(compilation).compile()

    for (notification in compilation.notifications) {
        getLogger("GroveDocCompiler").warning(notification.message + "\n" + notification.paths.joinToString("\n") + "\n")
    }
}