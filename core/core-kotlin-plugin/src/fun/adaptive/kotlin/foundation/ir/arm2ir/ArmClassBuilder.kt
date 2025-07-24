/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */


package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.kotlin.common.*
import `fun`.adaptive.kotlin.foundation.FoundationPluginKey
import `fun`.adaptive.kotlin.foundation.Names
import `fun`.adaptive.kotlin.foundation.Strings
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClass
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmDefaultValueStatement
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmInternalStateVariable
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmRenderingStatement
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
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
            visibility = DescriptorVisibilities.LOCAL
            modality = Modality.FINAL
        }

        irClass.superTypes = listOf(pluginContext.adaptiveFragmentType)
        irClass.metadata = armClass.originalFunction.metadata
        irClass.thisReceiver()
        val constructorFun = constructor()
        val initFun = initializer()

        irClass.parent = armClass.originalFunction

        constructorBody(constructorFun)

        irClass.addFakeOverrides(IrTypeSystemContextImpl(irContext.irBuiltIns))

        initializerBody(initFun)

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

    private fun constructorBody(constructorFun: IrConstructor) {

        constructorFun.body = irFactory.createBlockBody(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET).apply {

            statements += IrDelegatingConstructorCallImpl(
                SYNTHETIC_OFFSET,
                SYNTHETIC_OFFSET,
                pluginContext.adaptiveFragmentType,
                pluginContext.adaptiveFragmentClass.constructors.first(),
                typeArgumentsCount = 0
            ).apply {
                arguments[0] = irGet(constructorFun.firstRegularParameter)
                arguments[1] = irGet(constructorFun.secondRegularParameter)
                arguments[2] = irGet(constructorFun.thirdRegularParameter)
                arguments[3] = irConst(armClass.stateVariables.size)
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
        }

    private fun initializerBody(initFun: IrAnonymousInitializer) {
        initFun.body = DeclarationIrBuilder(irContext, initFun.symbol).irBlockBody {
            addLifecycleBound()
        }
    }

    private fun IrBlockBodyBuilder.addLifecycleBound() {
        val lifecycleBound = armClass.stateVariables.filter { it.type.isSubtypeOfClass(pluginContext.lifecycleBoundClass) }
        if (lifecycleBound.isEmpty()) return

        + irSetValue(
            irClass.property(Names.LIFECYCLE_BOUND),
            irIntArrayOf(lifecycleBound.map { it.indexInState }),
            irGet(irClass.thisReceiver !!)
        )
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

        buildFun.origin = IrDeclarationOrigin.DEFINED
        buildFun.isFakeOverride = false

        buildFun.replaceDispatchReceiver(irClass.defaultType)

        buildFun.body = DeclarationIrBuilder(irContext, buildFun.symbol).irBlockBody {

            if (armClass.rendering.isEmpty()) {
                + irReturn(irNull())
                return@irBlockBody
            }

            val fragment = irTemporary(genBuildWhen(buildFun))

            genBuildCreate(fragment, buildFun)

            + irReturn(irGet(fragment))
        }
    }

    private fun IrBlockBodyBuilder.genBuildCreate(fragment: IrVariable, buildFun: IrFunction) {
        val condition = irEqual(
            irAnd(irGet(buildFun.thirdRegularParameter), irConst(AdaptiveFragment.DETACHED_MASK)),
            irConst(0)
        )

        val body = IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.create,
            typeArgumentsCount = 0
        ).also {
            it.dispatchReceiver = irGet(fragment)
        }

        + irIf(condition, body)
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

            branches += irInvalidIndexBranch(buildFun, irGet(buildFun.secondRegularParameter))
        }

    private fun genBuildWhenBranch(buildFun: IrSimpleFunction, renderingStatement: ArmRenderingStatement) =

        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(buildFun.secondRegularParameter),
                irConst(renderingStatement.index)
            ),
            renderingStatement.branchBuilder(this@ArmClassBuilder).genBuildConstructorCall(buildFun)
        )

    // ---------------------------------------------------------------------------
    // Patch Descendants
    // ---------------------------------------------------------------------------

    fun genPatchDescendantBody() {
        val patchFun = irClass.getSimpleFunction(Strings.GEN_PATCH_DESCENDANT) !!.owner

        patchFun.origin = IrDeclarationOrigin.DEFINED
        patchFun.isFakeOverride = false

        patchFun.replaceDispatchReceiver(irClass.defaultType)

        patchFun.body = DeclarationIrBuilder(irContext, patchFun.symbol).irBlockBody {

            val closureMask = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.getCreateClosureDirtyMask,
                    typeArgumentsCount = 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.firstRegularParameter)
                }
            )

            val fragmentIndex = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    pluginContext.index.single().owner.getter !!.symbol,
                    typeArgumentsCount = 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.firstRegularParameter)
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

        patchFun.origin = IrDeclarationOrigin.DEFINED
        patchFun.isFakeOverride = false

        patchFun.replaceDispatchReceiver(irClass.defaultType)

        patchFun.body = DeclarationIrBuilder(irContext, patchFun.symbol).irBlockBody {

            val dirtyMask = irTemporary(
                IrCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    irBuiltIns.intType,
                    irClass.propertyGetter { "dirtyMask" },
                    typeArgumentsCount = 0
                ).also {
                    it.dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!)
                },
                isMutable = true
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
                val originalExpression = when (statement) {
                    is ArmDefaultValueStatement -> statement.defaultValue
                    else -> statement.irStatement as IrExpression
                }

                val transformedExpression = originalExpression
                    .transformThisStateAccess(
                        armClass.stateVariables,
                        newParent = patchFun
                    ) { irGet(patchFun.dispatchReceiverParameter !!) }

                // optimize out null default values
                if (transformedExpression is IrConstImpl && transformedExpression.kind == IrConstKind.Null) continue

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

            + IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.intType,
                irClass.getPropertySetter("dirtyMask") !!,
                typeArgumentsCount = 0
            ).also {
                it.arguments[0] = irGet(patchFun.dispatchReceiverParameter !!)
                it.arguments[1] = irGet(dirtyMask)
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
                typeArgumentsCount = 0
            ).also {
                it.arguments[0]  = irGet(fromFun.dispatchReceiverParameter !!)
                it.arguments[1] = getIndex
            }
        )

    // ---------------------------------------------------------------------------
    // Original function body
    // ---------------------------------------------------------------------------

    fun buildOriginalFunctionBody() {
        // transform the original function
        // - replace the parameters with parent, index
        // - change return type to AdaptiveFragment
        // - replace the body with:
        //    - armClass declaration
        //    - create an instance of the class
        //    - return with the created instance

        val irClass = armClass.irClass

        with(armClass.originalFunction) {
            parameters = emptyList()

            val parent = addValueParameter(Strings.PARENT, pluginContext.adaptiveFragmentType)
            val index = addValueParameter(Strings.DECLARATION_INDEX, irBuiltIns.intType)

            returnType = pluginContext.adaptiveFragmentType

            body = DeclarationIrBuilder(pluginContext.irContext, originalFunction.symbol).irBlockBody {
                + irClass
                + irReturn(
                    IrConstructorCallImpl(
                        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                        irClass.defaultType,
                        irClass.constructors.first().symbol,
                        typeArgumentsCount = 0,
                        constructorTypeArgumentsCount = 0
                    ).also {
                        it.arguments[0] = irGetValue(pluginContext.adapter, irGet(parent))
                        it.arguments[1] = irGet(parent)
                        it.arguments[2] = irGet(index)
                    }
                )
            }
        }
    }
}