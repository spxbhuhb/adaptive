/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.base.fir

import hu.simplexion.adaptive.kotlin.adat.AdatPluginKey
import hu.simplexion.adaptive.kotlin.base.BasePluginKey
import hu.simplexion.adaptive.kotlin.base.ClassIds
import hu.simplexion.adaptive.kotlin.base.Names
import hu.simplexion.adaptive.kotlin.base.ir.util.capitalizeFirstChar
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.*
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.plugin.createConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createTopLevelClass
import org.jetbrains.kotlin.fir.scopes.impl.toConeType
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.*

class AdaptiveDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    val nullableAnyType = ClassIds.KOTLIN_ANY.constructClassLikeType(emptyArray(), true)
    val argumentsType = ClassIds.KOTLIN_ARRAY.constructClassLikeType(arrayOf(nullableAnyType), false)

    val supportFunctionType = ClassIds.ADAPTIVE_SUPPORT_FUNCTION.constructClassLikeType(emptyArray(), false)

    val ADAPTIVE_PREDICATE = LookupPredicate.create { annotated(ClassIds.ADAPTIVE.asSingleFqName()) }

    private val predicateBasedProvider = session.predicateBasedProvider

    private val matchedFunctions by lazy {
        predicateBasedProvider
            .getSymbolsByPredicate(ADAPTIVE_PREDICATE)
            .filterIsInstance<FirNamedFunctionSymbol>()
    }

    private val classIdsForMatchedFunctions: Set<ClassId> by lazy {
        matchedFunctions.map {
            ClassId(it.callableId.packageName, Name.identifier("Adaptive" + it.name.identifier.capitalizeFirstChar()))
        }.toSet()
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(ADAPTIVE_PREDICATE)
    }

    override fun getTopLevelClassIds(): Set<ClassId> {
        return if (matchedFunctions.isEmpty()) emptySet() else classIdsForMatchedFunctions
    }

    override fun generateTopLevelClassLikeDeclaration(classId: ClassId): FirClassLikeSymbol<*> =
        createTopLevelClass(
            classId,
            BasePluginKey
        ) {
            typeParameter(Names.BT)
            superType { ClassIds.ADAPTIVE_FRAGMENT.constructClassLikeType(arrayOf(it.first().toConeType()), false) }
        }.symbol

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> =
        setOf(
            SpecialNames.INIT,
            Names.GEN_BUILD,
            Names.GEN_PATCH_DESCENDANT,
            Names.GEN_INVOKE,
            Names.GEN_INVOKE_SUSPEND
        )

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> =
        listOf(
            createConstructor(context.owner, BasePluginKey, isPrimary = true, generateDelegatedNoArgConstructorCall = false) {
                valueParameter(Names.ADAPTER, context.adapterType())
                valueParameter(Names.PARENT, context.nullableFragmentType())
                valueParameter(Names.INDEX, session.builtinTypes.intType.coneType)
            }.symbol
        )

    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        return when (callableId.callableName) {

            Names.GEN_BUILD -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, context.nullableFragmentType()) {
                        valueParameter(Names.PARENT, context.fragmentType())
                        valueParameter(Names.INDEX, session.builtinTypes.intType.coneType)
                    }.symbol
                )
            }

            Names.GEN_PATCH_DESCENDANT -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, session.builtinTypes.unitType.coneType) {
                        valueParameter(Names.FRAGMENT, context.fragmentType())
                    }.symbol
                )
            }

            Names.GEN_INVOKE -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, session.builtinTypes.nullableAnyType.coneType) {
                        valueParameter(Names.SUPPORT_FUNCTION, supportFunctionType)
                        valueParameter(Names.ARGUMENTS, argumentsType)
                    }.symbol
                )
            }

            Names.GEN_INVOKE_SUSPEND -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, session.builtinTypes.nullableAnyType.coneType) {
                        valueParameter(Names.SUPPORT_FUNCTION, supportFunctionType)
                        valueParameter(Names.ARGUMENTS, argumentsType)
                    }.symbol
                )
            }

            else -> emptyList()
        }
    }

    override fun hasPackage(packageFqName: FqName): Boolean {
        return true
    }

    fun MemberGenerationContext.adapterType() =
        ClassIds.ADAPTIVE_ADAPTER.constructClassLikeType(arrayOf(owner.typeParameterSymbols.first().toConeType()), false)

    fun MemberGenerationContext.fragmentType() =
        ClassIds.ADAPTIVE_FRAGMENT.constructClassLikeType(arrayOf(owner.typeParameterSymbols.first().toConeType()), false)

    fun MemberGenerationContext.nullableFragmentType() =
        ClassIds.ADAPTIVE_FRAGMENT.constructClassLikeType(arrayOf(owner.typeParameterSymbols.first().toConeType()), true)

}
