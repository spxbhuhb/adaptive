/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.common

import `fun`.adaptive.kotlin.AdaptiveOptions
import `fun`.adaptive.kotlin.adat.ClassIds
import `fun`.adaptive.kotlin.common.AdaptiveFqNames.PLUGIN_REFERENCE
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrFieldSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.FakeOverridesStrategy
import org.jetbrains.kotlin.ir.util.KotlinLikeDumpOptions
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.fields
import org.jetbrains.kotlin.ir.util.getAnnotationArgumentValue
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

abstract class AbstractPluginContext(
    val irContext: IrPluginContext,
    val options: AdaptiveOptions
) {

    val pluginLogDir: Path? = options.pluginLogDir?.let { options.pluginLogDir.toPath().also { it.createDirectories() } }
    val pluginLogTimestamp: String = DateTimeFormatter.ofPattern("yyyyMMdd'-'HHmmss").format(LocalDateTime.now())
    val pluginLogFile: Path? = debugFile()

    val kotlinSymbols by lazy { KotlinSymbols(irContext) }
    val kotlinUnsignedSymbols by lazy { KotlinUnsignedSymbols(irContext) }
    val dateTimeTypes by lazy { DateTimeTypes(irContext) }
    val adaptiveSymbols by lazy { AdaptiveSymbols(irContext) }

    val adatClass = ClassIds.ADAT_CLASS.classSymbol()

    fun ClassId.classSymbol() =
        checkNotNull(irContext.referenceClass(this)) {
            "Missing ${this.asFqNameString()} class. Maybe the gradle dependency on \"fun.adaptive:core-core\" is missing."
        }

    fun CallableId.propertyGetterSymbol() =
        checkNotNull(irContext.referenceProperties(this).first().owner.getter?.symbol) {
            "Missing ${this.asSingleFqName()}, is the plugin added to gradle?"
        }

    fun CallableId.firstFunctionSymbol() =
        checkNotNull(irContext.referenceFunctions(this).first()) {
            "Missing ${this.asSingleFqName()}, is the plugin added to gradle?"
        }

    fun IrClassSymbol.singleConstructor() =
        owner.constructors.single()

    fun IrClassSymbol.constructorByKey(key: String): IrConstructorSymbol =
        constructors.first { it.owner.getAnnotationArgumentValue<String>(PLUGIN_REFERENCE, "key") == key }

    fun IrClass.functionByKey(key: String): IrSimpleFunction =
        declarations.first { (it is IrSimpleFunction) && it.getAnnotationArgumentValue<String>(PLUGIN_REFERENCE, "key") == key } as IrSimpleFunction

    fun IrClassSymbol.fieldByName(name: String): IrFieldSymbol =
        fields.single { it.owner.name.asString() == name }

    fun ClassId.symbolOrNull(): IrClassSymbol? =
        irContext.referenceClass(this)

    fun CallableId.functions(): Collection<IrSimpleFunctionSymbol> =
        irContext.referenceFunctions(this)

    fun debugFile(): Path? {
        if (pluginLogDir == null) return null
        var postFix = 0
        while (postFix < 10) {
            val name = "adaptive-log-${this::class.simpleName !!.removeSuffix("PluginContext")}-$pluginLogTimestamp-$postFix"
            val path = pluginLogDir.resolve("$name.txt")
            if (! path.exists()) {
                return path
            }
            postFix ++
        }
        throw IllegalStateException("cannot create plugin log file, too many exists, dir: $pluginLogDir")
    }

    fun debug(label: String? = null, message: () -> Any?) {
        if (! options.pluginDebug) return

        val output = (if (label != null) "[$label]".padEnd(30) else "") + "${message()}\n"

        if (pluginLogFile != null) {
            Files.write(pluginLogFile, output.encodeToByteArray(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
        } else {
            println(output)
        }
    }

    fun dumpKotlinLike(fragment : IrModuleFragment) : String {
        return "\n\n" + fragment.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE))
    }
}