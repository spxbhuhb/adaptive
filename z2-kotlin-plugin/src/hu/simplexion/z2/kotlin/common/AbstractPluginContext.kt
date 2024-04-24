package hu.simplexion.z2.kotlin.common

import hu.simplexion.z2.kotlin.Z2Options
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrFieldSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.types.checker.SimpleClassicTypeSystemContext.getClassFqNameUnsafe
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

    val pluginLogDir: Path? = options.pluginLogDir?.let { options.pluginLogDir.toPath().also { it.createDirectories() } }
    val pluginLogTimestamp: String = DateTimeFormatter.ofPattern("yyyyMMdd'-'HHmmss").format(LocalDateTime.now())
    val pluginLogFile: Path? = debugFile()

    fun ClassId.classSymbol() =
        checkNotNull(irContext.referenceClass(this)) {
            "Missing ${this.asFqNameString()} class. Maybe the gradle dependency on \"hu.simplexion.z2:z2-core\" is missing."
        }

    fun CallableId.propertyGetterSymbol() =
        checkNotNull(irContext.referenceProperties(this).first().owner.getter?.symbol) {
            "Missing ${this.asSingleFqName()}, is the plugin added to gradle?"
        }

    fun CallableId.firstFunctionSymbol() =
        checkNotNull(irContext.referenceFunctions(this).first()) {
            "Missing ${this.asSingleFqName()}, is the plugin added to gradle?"
        }

    fun IrClassSymbol.property(nameFun: () -> String): IrProperty {
        val name = nameFun()
        return owner.properties.first { it.name.asString() == name }
    }

    fun IrClassSymbol.propertyGetter(nameFun: () -> String): IrSimpleFunctionSymbol {
        val name = nameFun()
        return checkNotNull(getPropertyGetter(name)) { "Missing property getter for in ${this.getClassFqNameUnsafe()} $name" }
    }

    fun IrClassSymbol.functionByName(nameFun: () -> String): IrSimpleFunctionSymbol {
        val name = nameFun()
        return functions.single { it.owner.name.asString() == name }
    }

    fun IrClassSymbol.singleConstructor() =
        owner.constructors.single()

    fun IrClassSymbol.fieldByName(name: String): IrFieldSymbol =
        fields.single { it.owner.name.asString() == name }

    fun debugFile(): Path? {
        if (pluginLogDir == null) return null
        var postFix = 0
        while (postFix < 10) {
            val name = "z2-log-${this::class.simpleName !!.removeSuffix("PluginContext")}-$pluginLogTimestamp-$postFix"
            val path = pluginLogDir.resolve("$name.txt")
            if (! path.exists()) {
                println("plugin debug log file: ${pluginLogFile?.toAbsolutePath()}")
                return path
            }
            postFix ++
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