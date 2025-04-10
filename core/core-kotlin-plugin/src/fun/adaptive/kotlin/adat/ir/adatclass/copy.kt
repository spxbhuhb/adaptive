package `fun`.adaptive.kotlin.adat.ir.adatclass

import `fun`.adaptive.kotlin.adat.ir.AdatIrBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.properties

fun AdatIrBuilder.copy(
    adatClass: IrClass,
    copyFunction: IrFunction
) {
    val primary = adatClass.constructors.first { it.isPrimary }
    val properties = adatClass.properties

    for (valueParameter in copyFunction.valueParameters) {
        valueParameter.defaultValue = irFactory.createExpressionBody(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irGetField(
                irGet(copyFunction.dispatchReceiverParameter!!),
                properties.first { it.symbol.owner.name == valueParameter.name }.backingField !!
            )
        )
    }

    copyFunction.body = DeclarationIrBuilder(irContext, copyFunction.symbol).irBlockBody {
        + irReturn(
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                copyFunction.returnType,
                primary.symbol,
                typeArgumentsCount = 0,
                constructorTypeArgumentsCount = 0
            ).also {
                for (index in 0 until copyFunction.valueParameters.size) {
                    it.putValueArgument(index, irGet(copyFunction.valueParameters[index]))
                }
            }
        )
    }
}

// code generated for a standard Kotlin data class:
//
// FUN GENERATED_DATA_CLASS_MEMBER name:copy visibility:public modality:FINAL <> ($this:fun.adaptive.adat.TestAdat, someInt:kotlin.Int, someBoolean:kotlin.Boolean, someIntListSet:kotlin.collections.Set<kotlin.collections.List<kotlin.Int>>) returnType:fun.adaptive.adat.TestAdat
//        $this: VALUE_PARAMETER name:<this> type:fun.adaptive.adat.TestAdat
//        VALUE_PARAMETER name:someInt index:0 type:kotlin.Int
//          EXPRESSION_BODY
//            GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:someInt type:kotlin.Int visibility:private' type=kotlin.Int origin=null
//              receiver: GET_VAR '<this>: fun.adaptive.adat.TestAdat declared in fun.adaptive.adat.TestAdat.copy' type=fun.adaptive.adat.TestAdat origin=null
//        VALUE_PARAMETER name:someBoolean index:1 type:kotlin.Boolean
//          EXPRESSION_BODY
//            GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:someBoolean type:kotlin.Boolean visibility:private' type=kotlin.Boolean origin=null
//              receiver: GET_VAR '<this>: fun.adaptive.adat.TestAdat declared in fun.adaptive.adat.TestAdat.copy' type=fun.adaptive.adat.TestAdat origin=null
//        VALUE_PARAMETER name:someIntListSet index:2 type:kotlin.collections.Set<kotlin.collections.List<kotlin.Int>>
//          EXPRESSION_BODY
//            GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:someIntListSet type:kotlin.collections.Set<kotlin.collections.List<kotlin.Int>> visibility:private' type=kotlin.collections.Set<kotlin.collections.List<kotlin.Int>> origin=null
//              receiver: GET_VAR '<this>: fun.adaptive.adat.TestAdat declared in fun.adaptive.adat.TestAdat.copy' type=fun.adaptive.adat.TestAdat origin=null
//        BLOCK_BODY
//          RETURN type=kotlin.Nothing from='public final fun copy (someInt: kotlin.Int, someBoolean: kotlin.Boolean, someIntListSet: kotlin.collections.Set<kotlin.collections.List<kotlin.Int>>): fun.adaptive.adat.TestAdat declared in fun.adaptive.adat.TestAdat'
//            CONSTRUCTOR_CALL 'public constructor <init> (someInt: kotlin.Int, someBoolean: kotlin.Boolean, someIntListSet: kotlin.collections.Set<kotlin.collections.List<kotlin.Int>>) [primary] declared in fun.adaptive.adat.TestAdat' type=fun.adaptive.adat.TestAdat origin=null
//              someInt: GET_VAR 'someInt: kotlin.Int declared in fun.adaptive.adat.TestAdat.copy' type=kotlin.Int origin=null
//              someBoolean: GET_VAR 'someBoolean: kotlin.Boolean declared in fun.adaptive.adat.TestAdat.copy' type=kotlin.Boolean origin=null
//              someIntListSet: GET_VAR 'someIntListSet: kotlin.collections.Set<kotlin.collections.List<kotlin.Int>> declared in fun.adaptive.adat.TestAdat.copy' type=kotlin.collections.Set<kotlin.collections.List<kotlin.Int>> origin=null

