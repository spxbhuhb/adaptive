package hu.simplexion.z2.kotlin.util

import hu.simplexion.z2.kotlin.Z2Options
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

abstract class AbstractPluginContext(
    val irContext: IrPluginContext,
    val options: Z2Options
) {

    abstract val runtimePackage: String

    val pluginLogDir: Path? = options.pluginLogDir?.let { options.pluginLogDir.toPath().also { it.createDirectories() } }
    val pluginLogTimestamp: String = DateTimeFormatter.ofPattern("yyyyMMdd'-'HHmmss").format(LocalDateTime.now())
    val pluginLogFile: Path? = debugFile()

    val stringType by lazy { irContext.irBuiltIns.stringType }

    val listClass by lazy { LIST.runtimeClass(KOTLIN_COLLECTIONS) }
    val uuidClass by lazy { UUID.runtimeClass(UTIL_PACKAGE) }

    fun String.runtimeClass(pkg: String = runtimePackage) =
        checkNotNull(irContext.referenceClass(ClassId(FqName(pkg), Name.identifier(this)))) {
            "Missing ${pkg}.$this class. Maybe the gradle dependency on \"hu.simplexion.z2:z2-core\" is missing."
        }

    fun classSymbol(name: FqName): IrClassSymbol =
        name.shortName().toString().runtimeClass(name.parent().asString())

    fun debugFile() : Path? {
        if (pluginLogDir == null) return null
        var postFix = 0
        while (postFix < 10) {
            val name = "z2-log-${this::class.simpleName!!.removeSuffix("PluginContext")}-$pluginLogTimestamp-$postFix"
            val path = pluginLogDir.resolve("$name.txt")
            if (!path.exists()) {
                println("plugin debug log file: ${pluginLogFile?.toAbsolutePath()}")
                return path
            }
            postFix++
        }
        throw IllegalStateException("cannot create plugin log file, too many exists, dir: $pluginLogDir")
    }

    fun debug(label: String, message: () -> Any?) {
        if (! options.pluginDebug) return

        val paddedLabel = "[$label]".padEnd(30)
        val output = "$paddedLabel  ${message()}\n"

        if (pluginLogFile != null) {
            Files.write(pluginLogFile, output.encodeToByteArray(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
        } else {
            println(output)
        }
    }
}