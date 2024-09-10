package `fun`.adaptive.kotlin.adat.ir.metadata

import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.kotlin.adat.FqNames
import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import `fun`.adaptive.kotlin.common.removeCoercionToUnit
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.getAnnotationArgumentValue
import org.jetbrains.kotlin.ir.util.statements

fun AdatIrBuilder.descriptor(
    adatClass: IrClass,
    descriptorFunction: IrFunction,
    result : MutableList<Pair<String,List<AdatDescriptorMetadata>>>
) {
    for (statement in getStatements(adatClass, descriptorFunction)) {
        result += collectPropertyDescriptors(adatClass, statement)
    }

    // the descriptor body is moved into the metadata
    descriptorFunction.body = DeclarationIrBuilder(irContext, descriptorFunction.symbol).irBlockBody {  }
}

private fun AdatIrBuilder.getStatements(
    adatClass: IrClass,
    descriptorFunction: IrFunction
): List<IrStatement> {
    val body = descriptorFunction.body
    requireNotNull(body) { "missing descriptor function body in ${adatClass.fqNameWhenAvailable}" }

    require(body.statements.size == 1) { "descriptor function body must have exactly one statement in ${adatClass.fqNameWhenAvailable}" }

    val properties = body.statements.first()
    require(properties is IrCall) { "descriptor function body must have a single 'properties' call in ${adatClass.fqNameWhenAvailable}" }

    require(properties.symbol == pluginContext.propertiesFun) { "descriptor function body must have a single 'properties' call in ${adatClass.fqNameWhenAvailable}" }

    val propertiesArg = properties.getValueArgument(0)
    require(propertiesArg is IrFunctionExpression) { "properties call must have a function expression argument in ${adatClass.fqNameWhenAvailable}" }

    val propertiesBody = propertiesArg.function.body
    requireNotNull(propertiesBody)

    return propertiesBody.statements
}

private fun collectPropertyDescriptors(
    adatClass: IrClass,
    statement: IrStatement
): Pair<String, List<AdatDescriptorMetadata>> {

    val call = statement.removeCoercionToUnit()

    require(call is IrCall) { "invalid descriptor statement (not a call) ${call.dumpKotlinLike()}" }

    var propertyName: String? = null
    val result = mutableListOf<AdatDescriptorMetadata>()
    var current: IrExpression? = call

    while (current != null) {

        when (current) {

            is IrGetValue -> {
                val owner = current.symbol.owner
                check(owner.parent == adatClass) { "invalid descriptor property base: ${current.dumpKotlinLike()}" }
                propertyName = owner.name.identifier
                current = null // get value must be the last
            }

            is IrCall -> {
                val owner = current.symbol.owner

                val receiver = current.extensionReceiver

                if (receiver != null) {

                    result += AdatDescriptorMetadata(
                        owner.getAnnotationArgumentValue<String>(FqNames.ADAT_DESCRIPTOR_NAME, "name") !!,
                        encodeDescriptorParameters(current)
                    )

                    current = current.extensionReceiver

                } else {

                    val symbol = current.symbol.owner.correspondingPropertySymbol
                    checkNotNull(symbol) { "not a property access (1): ${call.dumpKotlinLike()}" }
                    propertyName = symbol.owner.name.identifier
                    break
                }

            }

            else -> {
                throw IllegalStateException("unknown access: ${statement.dumpKotlinLike()}")
            }
        }
    }

    check(propertyName != null) { "not a property access (2): ${statement.dumpKotlinLike()}" }

    return propertyName to result.reversed()
}

private fun encodeDescriptorParameters(current: IrCall): String {
    check(current.valueArgumentsCount == 1) { "only single parameter descriptors are supported: ${current.dumpKotlinLike()}" }

    val const = current.getValueArgument(0)
    check(const is IrConstImpl<*>) { "only constants are supported as descriptor parameters: ${current.dumpKotlinLike()}" }

    return const.value.toString()
}