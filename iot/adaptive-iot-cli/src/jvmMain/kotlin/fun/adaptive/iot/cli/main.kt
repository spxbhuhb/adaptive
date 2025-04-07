package `fun`.adaptive.iot.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

class AdaptiveCli : CliktCommand(name = "aio-cli") {
    override fun run() = Unit
}

fun main(args: Array<String>) {
    AdaptiveCli()
        .subcommands(
            CurValUpload(),
            SimHistory(),
            UploadTextHistory()
        )
        .main(args)
}
