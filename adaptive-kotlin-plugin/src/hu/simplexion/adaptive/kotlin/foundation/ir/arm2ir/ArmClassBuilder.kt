/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */


package hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir

import hu.simplexion.adaptive.kotlin.common.propertyGetter
import hu.simplexion.adaptive.kotlin.foundation.FoundationPluginKey
import hu.simplexion.adaptive.kotlin.foundation.Indices
import hu.simplexion.adaptive.kotlin.foundation.Names
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.FoundationPluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmClass
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmDefaultValueStatement
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmInternalStateVariable
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmRenderingStatement
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addConstructor
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.symbols.impl.IrAnonymousInitializerSymbolImpl
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*

@OptIn(UnsafeDuringIrConstructionAPI::class) // uncomment for 2.0.0
class ArmClassBuilder(
    context: FoundationPluginContext,
    armClass: ArmClass
) : ClassBoundIrBuilder(context, armClass) {

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

        irClass.superTypes = listOf(pluginContext.adaptiveFragmentType)
        irClass.metadata = armClass.originalFunction.metadata
        irClass.thisReceiver()
        constructor()
        initializer()

        irClass.parent = originalFunction.file

        constructorBody()

        armClass.stateInterface?.let {
            irClass.superTypes += it.defaultType
        }

        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))

        armClass.irClass = irClass
        pluginContext.irClasses[armClass.fqName] = irClass
    }

    // --------------------------------------------------------------------------------------------------------
    // Declaration and initialization
    // --------------------------------------------------------------------------------------------------------

    private fun constructor(): IrConstructor =
        irClass.addConstructor {
            isPrimary = true
            returnType = irClass.typeWith()
        }.apply {
            parent = irClass

            addValueParameter {
                name = Names.ADAPTER
                type = pluginContext.adaptiveAdapterType
            }

            addValueParameter {
                name = Names.PARENT
                type = pluginContext.adaptiveFragmentNType
            }

            addValueParameter {
                name = Names.DECLARATION_INDEX
                type = irBuiltIns.intType
            }
        }

    private fun constructorBody() {
        val constructor = irClass.constructors.first()

        constructor.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

            statements += IrDelegatingConstructorCallImpl.fromSymbolOwner(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                pluginContext.adaptiveFragmentType,
                pluginContext.adaptiveFragmentClass.constructors.first(),
                typeArgumentsCount = 0,
                valueArgumentsCount = 5
            ).apply {
                putValueArgument(0, irGet(constructor.valueParameters[0]))
                putValueArgument(1, irGet(constructor.valueParameters[1]))
                putValueArgument(2, irGet(constructor.valueParameters[2]))
                putValueArgument(3, irConst(instructionIndex()))
                putValueArgument(4, irConst(armClass.stateVariables.size))
            }

            statements += IrInstanceInitializerCallImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                irClass.symbol,
                irBuiltIns.unitType
            )
        }
    }

    private fun instructionIndex(): Int =
        armClass.stateVariables.firstOrNull { it.isInstructions }?.indexInState ?: - 1

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
            pluginContext.adaptiveFragmentType,
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
    // Patch Internal
    // ---------------------------------------------------------------------------

    fun genPatchInternalBody() {
        val patchFun = irClass.getSimpleFunction(Strings.GEN_PATCH_INTERNAL) !!.owner

        patchFun.isFakeOverride = false

        patchFun.body = DeclarationIrBuilder(irContext, patchFun.symbol).irBlockBody {

            val dirtyMask = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    irClass.propertyGetter { "dirtyMask" },
                    0, 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!)
                }
            )

            for (statement in armClass.stateDefinitionStatements) {
                when (statement) {
                    is ArmInternalStateVariable -> {
                        statement.builder(this@ArmClassBuilder).genPatchInternal(this, dirtyMask, patchFun)
                        continue
                    }
                }

                // FIXME casting a statement into an expression in internal patch
                // FIXME apply the same pattern as for ArmInternalStateVariable
                val (originalExpression, stateVariable) = when (statement) {
                    is ArmDefaultValueStatement -> statement.defaultValue to null
                    else -> statement.irStatement as IrExpression to null
                }

                val transformedExpression = originalExpression
                    .transformThisStateAccess(armClass.stateVariables, newParent = patchFun, stateVariable = stateVariable) { irGet(patchFun.dispatchReceiverParameter !!) }

                // optimize out null default values
                if (transformedExpression is IrConstImpl<*> && transformedExpression.kind == IrConstKind.Null) continue

                + irIf(
                    when (statement) {
                        is ArmDefaultValueStatement -> genPatchInternalConditionForDefault(patchFun, statement)
                        else -> genPatchInternalConditionForMask(patchFun, dirtyMask, statement.dependencies)
                    },
                    when (statement) {
                        is ArmDefaultValueStatement -> irSetStateVariable(patchFun, statement.indexInState, transformedExpression)
                        else -> transformedExpression
                    }
                )
            }

            + irReturn(irConst(true))
        }
    }

    fun genPatchInternalConditionForDefault(patchFun: IrSimpleFunction, statement: ArmDefaultValueStatement): IrExpression =
        irEqual(
            irGetThisStateVariable(patchFun, statement.indexInState),
            irNull()
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