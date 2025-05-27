package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.persistence.ensure
import kotlinx.io.files.Path

fun groveDocMain(args: Array<String>) : GroveDocCompilation {
    check(args.size == 3) { "usage: <in> <md-out> <values-out>" }

    val inPath = Path(args[0])
    val mdOutPath = Path(args[1]).ensure()
    val valuesOutPath = Path(args[2]).ensure()

    val compilation = GroveDocCompilation(
        inPath,
        mdOutPath,
        valuesOutPath
    )

    GroveDocCompiler(compilation).compile()

    for (notification in compilation.notifications) {
        getLogger("GroveDocCompiler").warning(notification.message + "\n" + notification.paths.joinToString("\n") + "\n")
    }

    return compilation
}