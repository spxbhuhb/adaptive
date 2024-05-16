/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir

import hu.simplexion.adaptive.kotlin.foundation.FoundationPluginKey
import hu.simplexion.adaptive.kotlin.foundation.Indices
import hu.simplexion.adaptive.kotlin.foundation.Names
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.AdaptivePluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.*
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrTypeParameterSymbolImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.types.Variance

class ArmClassBuilder(
    context: AdaptivePluginContext,
    val armClass: ArmClass
) : ClassBoundIrBuilder(context) {

    // --------------------------------------------------------------------------------------------------------
    // First step of class generation: everything without generated function bodies
    // --------------------------------------------------------------------------------------------------------

    fun buildIrClassWithoutGenBodies() {

        val originalFunction = armClass.originalFunction

        irClass = irContext.irFactory.buildClass {
                startOffset = originalFunction.startOffset
                endOffset = originalFunction.endOffset
                origin = FoundationPluginKey.origin
                name = armClass.name
                kind = ClassKind.CLASS
                visibility = originalFunction.visibility
                modality = Modality.OPEN
            }

        typeParameters()
        irClass.superTypes = listOf(pluginContext.adaptiveFragmentClass.typeWith(irClass.typeParameters.first().defaultType))
        irClass.metadata = armClass.originalFunction.metadata
        thisReceiver()
        constructor()
        initializer()

        irClass.parent = originalFunction.file

        constructorBody()

        armClass.stateInterface?.let {
            irClass.superTypes += it.defaultType
        }

        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))

        pluginContext.irClasses[armClass.fqName] = irClass
    }

    // --------------------------------------------------------------------------------------------------------
    // Declaration and initialization
    // --------------------------------------------------------------------------------------------------------

    private fun typeParameters() {
        irClass.typeParameters = listOf(
            irFactory.createTypeParameter(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                IrDeclarationOrigin.BRIDGE_SPECIAL,
                symbol = IrTypeParameterSymbolImpl(),
                name = Names.BT,
                index = 0,
                isReified = false,
                variance = Variance.IN_VARIANCE
            ).also {
                it.parent = irClass
                it.superTypes = listOf(irBuiltIns.anyNType)
            }
        )
    }

    private fun thisReceiver(): IrValueParameter =

        irFactory.createValueParameter(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            IrDeclarationOrigin.INSTANCE_RECEIVER,
            symbol = IrValueParameterSymbolImpl(),
            name = SpecialNames.THIS,
            index = UNDEFINED_PARAMETER_INDEX,
            type = IrSimpleTypeImpl(irClass.symbol, false, emptyList(), emptyList()),
            varargElementType = null,
            isCrossinline = false,
            isNoinline = false,
            isHidden = false,
            isAssignable = false
        ).also {
            it.parent = irClass
            irClass.thisReceiver = it
        }

    private fun constructor(): IrConstructor =
        irClass.addConstructor {
            isPrimary = true
            returnType = irClass.typeWith()
        }.apply {
            parent = irClass

            addValueParameter {
                name = Names.ADAPTER
                type = classBoundAdapterType
            }

            addValueParameter {
                name = Names.PARENT
                type = classBoundFragmentType.makeNullable()
            }

            addValueParameter {
                name = Names.INDEX
                type = irBuiltIns.intType
            }
        }

    private fun constructorBody() {
        val constructor = irClass.constructors.first()

        constructor.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

            statements += IrDelegatingConstructorCallImpl.fromSymbolOwner(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                pluginContext.adaptiveFragmentClass.typeWith(classBoundFragmentType),
                pluginContext.adaptiveFragmentClass.constructors.first(),
                typeArgumentsCount = 1,
                valueArgumentsCount = Indices.ADAPTIVE_FRAGMENT_ARGUMENT_COUNT
            ).apply {
                putTypeArgument(0, classBoundFragmentType)
                putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, irGet(constructor.valueParameters[0]))
                putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irGet(constructor.valueParameters[1]))
                putValueArgument(Indices.ADAPTIVE_FRAGMENT_INDEX, irGet(constructor.valueParameters[2]))
                putValueArgument(Indices.ADAPTIVE_FRAGMENT_STATE_SIZE, irConst(armClass.stateVariables.size))
            }

            statements += IrInstanceInitializerCallImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                irClass.symbol,
                irBuiltIns.unitType
            )
        }
    }

    private fun initializer(): IrAnonymousInitializer =
        irFactory.createAnonymousInitializer(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.DEFINED,
            symbol = IrAnonymousInitializerSymbolImpl(),
            isStatic = false
        ).also { initFun ->
            initFun.parent = irClass
            irClass.declarations += initFun

            initFun.body = DeclarationIrBuilder(irContext, initFun.symbol).irBlockBody {

            }
        }

    // --------------------------------------------------------------------------------------------------------
    // Second step of class generation: generated function bodies
    // --------------------------------------------------------------------------------------------------------

    fun buildGenFunctionBodies() {
        genBuildBody()
        genPatchDescendantBody()
        genInvokeBody(Strings.GEN_INVOKE)
        genInvokeBody(Strings.GEN_INVOKE_SUSPEND)
        genPatchInternalBody()
    }

    // ---------------------------------------------------------------------------
    // Build
    // ---------------------------------------------------------------------------

    fun genBuildBody() {
        val buildFun = irClass.getSimpleFunction(Strings.GEN_BUILD) !!.owner

        buildFun.isFakeOverride = false

        buildFun.body = DeclarationIrBuilder(irContext, buildFun.symbol).irBlockBody {

            if (armClass.rendering.isEmpty()) {
                + irReturn(irNull())
                return@irBlockBody
            }

            val fragment = irTemporary(genBuildWhen(buildFun))

            + IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.unitType,
                pluginContext.create,
                0, 0
            ).also {
                it.dispatchReceiver = irGet(fragment)
            }

            + irReturn(irGet(fragment))
        }
    }

    private fun genBuildWhen(buildFun: IrSimpleFunction): IrExpression =

        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            classBoundFragmentType,
            IrStatementOrigin.WHEN
        ).apply {

            armClass.rendering.forEach {
                branches += genBuildWhenBranch(buildFun, it)
            }

            branches += irInvalidIndexBranch(buildFun, irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX]))
        }

    private fun genBuildWhenBranch(buildFun: IrSimpleFunction, renderingStatement: ArmRenderingStatement) =

        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX]),
                irConst(renderingStatement.index)
            ),
            renderingStatement.branchBuilder(this@ArmClassBuilder).genBuildConstructorCall(buildFun)
        )

    // ---------------------------------------------------------------------------
    // Patch Descendants
    // ---------------------------------------------------------------------------

    fun genPatchDescendantBody() {
        val patchFun = irClass.getSimpleFunction(Strings.GEN_PATCH_DESCENDANT) !!.owner

        patchFun.isFakeOverride = false

        patchFun.body = DeclarationIrBuilder(irContext, patchFun.symbol).irBlockBody {

            val closureMask = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.getCreateClosureDirtyMask,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.valueParameters[Indices.PATCH_DESCENDANT_FRAGMENT])
                }
            )

            val fragmentIndex = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.index.single().owner.getter !!.symbol,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.valueParameters[Indices.PATCH_DESCENDANT_FRAGMENT])
                }
            )

            + genPatchDescendantWhen(patchFun, fragmentIndex, closureMask)
        }
    }

    private fun genPatchDescendantWhen(patchFun: IrSimpleFunction, fragmentIndex: IrVariable, closureMask: IrVariable): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            IrStatementOrigin.WHEN
        ).apply {

            armClass.rendering.forEach {
                branches += genPatchDescendantBranch(patchFun, it, fragmentIndex, closureMask)
            }

            branches += irInvalidIndexBranch(patchFun, irGet(fragmentIndex))
        }

    private fun genPatchDescendantBranch(patchFun: IrSimpleFunction, branch: ArmRenderingStatement, fragmentIndex: IrVariable, closureMask: IrVariable) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(fragmentIndex),
                irConst(branch.index)
            ),
            branch.branchBuilder(this).genPatchDescendantBranch(patchFun, closureMask)
        )

    // ---------------------------------------------------------------------------
    // Invoke and Invoke Suspend
    // ---------------------------------------------------------------------------

    fun genInvokeBody(funName: String) {
        val invokeFun = irClass.getSimpleFunction(funName) !!.owner

        if (! invokeFun.isSuspend && ! armClass.hasInvokeBranch) return
        if (invokeFun.isSuspend && ! armClass.hasInvokeSuspendBranch) return

        invokeFun.isFakeOverride = false

        invokeFun.body = DeclarationIrBuilder(irContext, invokeFun.symbol).irBlockBody {

            val supportFunctionIndex = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.boundSupportFunctionIndex,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(invokeFun.valueParameters[Indices.INVOKE_SUPPORT_FUNCTION])
                }
            )

            val receivingFragment = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    classBoundFragmentType,
                    pluginContext.boundSupportFunctionReceivingFragment,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(invokeFun.valueParameters[Indices.INVOKE_SUPPORT_FUNCTION])
                }
            )
            val arguments = irTemporary(
                irGet(invokeFun.valueParameters[Indices.INVOKE_ARGUMENTS])
            )

            + irReturn(genInvokeWhen(invokeFun, supportFunctionIndex, receivingFragment, arguments))
        }
    }

    private fun genInvokeWhen(invokeFun: IrSimpleFunction, supportFunctionIndex: IrVariable, receivingFragment: IrVariable, arguments: IrVariable): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.anyNType,
            IrStatementOrigin.WHEN
        ).apply {

            armClass.stateVariables.forEach { stateVariable ->
                val producer = (stateVariable as? ArmInternalStateVariable)?.producer ?: return@forEach
                if ((invokeFun.isSuspend && producer.isSuspend) || (! invokeFun.isSuspend && ! producer.isSuspend)) {
                    branches += producer.branchBuilder(this@ArmClassBuilder).genInvokeBranches(invokeFun, supportFunctionIndex, receivingFragment, arguments)
                }
            }

            armClass.rendering.forEach { branch ->
                if ((invokeFun.isSuspend && branch.hasInvokeSuspendBranch) || (! invokeFun.isSuspend && branch.hasInvokeBranch)) {
                    branches += branch.branchBuilder(this@ArmClassBuilder).genInvokeBranches(invokeFun, supportFunctionIndex, receivingFragment, arguments)
                }
            }

            branches += irInvalidIndexBranch(invokeFun, irGet(supportFunctionIndex))
        }

    // ---------------------------------------------------------------------------
    // Patch Internal
    // ---------------------------------------------------------------------------

    fun genPatchInternalBody() {
        val patchFun = irClass.getSimpleFunction(Strings.GEN_PATCH_INTERNAL) !!.owner

        patchFun.isFakeOverride = false

        patchFun.body = DeclarationIrBuilder(irContext, patchFun.symbol).irBlockBody {

            val closureMask = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.getThisClosureDirtyMask,
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!)
                }
            )

            for (statement in armClass.stateDefinitionStatements) {
                // FIXME casting a statement into an expression in internal patch
                val originalExpression = when (statement) {
                    is ArmInternalStateVariable -> statement.builder(this@ArmClassBuilder).genInitializer(patchFun)
                    is ArmDefaultValueStatement -> statement.defaultValue
                    else -> statement.irStatement as IrExpression
                }

                val transformedExpression = originalExpression.transformThisStateAccess(armClass.stateVariables) { irGet(patchFun.dispatchReceiverParameter !!) }

                // optimize out null default values
                if (transformedExpression is IrConstImpl<*> && transformedExpression.kind == IrConstKind.Null) continue

                + irIf(
                    when (statement) {
                        is ArmDefaultValueStatement -> genPatchInternalConditionForDefault(patchFun, statement)
                        else -> genPatchInternalConditionForMask(patchFun, closureMask, statement.dependencies)
                    },
                    when (statement) {
                        is ArmInternalStateVariable -> irSetStateVariable(patchFun, statement.indexInState, transformedExpression)
                        is ArmDefaultValueStatement -> irSetStateVariable(patchFun, statement.indexInState, transformedExpression)
                        else -> transformedExpression
                    }
                )
            }

            + irReturn(irUnit())
        }
    }

    fun genPatchInternalConditionForDefault(patchFun: IrSimpleFunction, statement: ArmDefaultValueStatement): IrExpression =
        irEqual(
            irGetThisStateVariable(patchFun, statement.indexInState),
            irNull()
        )

    fun genPatchInternalConditionForMask(patchFun: IrSimpleFunction, closureMask: IrVariable, dependencies: ArmDependencies): IrExpression =
        irCall(
            symbol = pluginContext.haveToPatch,
            dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!),
            args = arrayOf(
                irGet(closureMask),
                dependencies.toDirtyMask()
            )
        )

    // ---------------------------------------------------------------------------
    // Common
    // ---------------------------------------------------------------------------

    private fun irInvalidIndexBranch(fromFun: IrSimpleFunction, getIndex: IrExpression) =
        IrElseBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.nothingType,
                irClass.getSimpleFunction(Strings.INVALID_INDEX) !!,
                0, 1
            ).also {
                it.dispatchReceiver = irGet(fromFun.dispatchReceiverParameter !!)
                it.putValueArgument(
                    Indices.INVALID_INDEX_INDEX,
                    getIndex
                )
            }
        )
}