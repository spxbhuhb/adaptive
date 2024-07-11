/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.adat.fir

import hu.simplexion.adaptive.kotlin.adat.AdatPluginKey
import hu.simplexion.adaptive.kotlin.adat.ClassIds
import hu.simplexion.adaptive.kotlin.adat.FqNames
import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.common.isFromPlugin
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingClassSymbol
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.utils.isCompanion
import org.jetbrains.kotlin.fir.extensions.*
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.plugin.createCompanionObject
import org.jetbrains.kotlin.fir.plugin.createConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createMemberProperty
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.getSuperTypes
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

class AdatDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    val nullableAnyType = ClassIds.KOTLIN_ANY.constructClassLikeType(emptyArray(), true)
    val nullableAnyArrayType = ClassIds.KOTLIN_ARRAY.constructClassLikeType(arrayOf(nullableAnyType), false)
    val ADAT_PREDICATE = LookupPredicate.create { annotated(FqNames.ADAT_ANNOTATION) }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(ADAT_PREDICATE)
    }

    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {
        if (classSymbol.isAdatClass) {
            return setOf(SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT)
        } else {
            return emptySet()
        }
    }

    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {
        if (! owner.isAdatClass) return null

        check(owner is FirRegularClassSymbol)

        return when (name) {
            SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT -> generateCompanionDeclaration(owner)
            else -> error("Can't generate class ${owner.classId.createNestedClassId(name).asSingleFqName()}")
        }
    }

    private fun generateCompanionDeclaration(owner: FirRegularClassSymbol): FirRegularClassSymbol? {
        if (owner.companionObjectSymbol != null || owner.isCompanion) return null

        val companion = createCompanionObject(owner, AdatPluginKey) {
            superType(ClassIds.ADAT_COMPANION.constructClassLikeType(arrayOf(owner.defaultType()), false))
        }

        return companion.symbol
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        return when {
            classSymbol.isAdatClass -> {
                setOf(
                    Names.GEN_GET_VALUE,
                    Names.GEN_SET_VALUE,
                    Names.EQUALS,
                    Names.HASHCODE,
                    Names.TO_STRING,
                    Names.ADAT_COMPANION,
                    SpecialNames.INIT
                )
            }

            classSymbol.isAdatCompanion -> {
                setOf(
                    Names.ADAT_METADATA,
                    Names.ADAT_WIREFORMAT,
                    Names.WIREFORMAT_NAME,
                    Names.NEW_INSTANCE,
                    SpecialNames.INIT
                )
            }

            else -> emptySet()
        }
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        when {
            context.isAdatClass -> {
                return listOf(
                    createConstructor(context.owner, AdatPluginKey, isPrimary = false, generateDelegatedNoArgConstructorCall = true).symbol,
                    createConstructor(context.owner, AdatPluginKey, isPrimary = false, generateDelegatedNoArgConstructorCall = true) {
                        valueParameter(Names.VALUES, nullableAnyArrayType)
                    }.symbol
                )
            }

            context.isAdatCompanion -> {
                return listOf(
                    createConstructor(context.owner, AdatPluginKey, isPrimary = true, generateDelegatedNoArgConstructorCall = true).symbol
                )
            }

            else -> return emptyList()
        }

    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        return when (callableId.callableName) {

            Names.ADAT_COMPANION -> {
                listOf(
                    createMemberProperty(
                        context !!.owner,
                        AdatPluginKey,
                        Names.ADAT_COMPANION,
                        ClassIds.ADAT_COMPANION.constructClassLikeType(arrayOf(context.adatClassType), false),
                        isVal = true,
                        hasBackingField = false
                    ).symbol
                )
            }

            Names.ADAT_METADATA -> {
                listOf(
                    createMemberProperty(
                        context !!.owner,
                        AdatPluginKey,
                        Names.ADAT_METADATA,
                        ClassIds.ADAT_CLASS_METADATA.constructClassLikeType(arrayOf(context.adatClassType), false),
                        isVal = true,
                        hasBackingField = true
                    ).symbol
                )
            }

            Names.ADAT_WIREFORMAT -> {
                val wireFormatClassId = hu.simplexion.adaptive.kotlin.wireformat.ClassIds.WIREFORMAT

                listOf(
                    createMemberProperty(
                        context !!.owner,
                        AdatPluginKey,
                        Names.ADAT_WIREFORMAT,
                        wireFormatClassId.constructClassLikeType(arrayOf(context.adatClassType), false),
                        isVal = true,
                        hasBackingField = true
                    ).symbol
                )
            }

            Names.WIREFORMAT_NAME -> {
                listOf(
                    createMemberProperty(
                        context !!.owner,
                        AdatPluginKey,
                        Names.WIREFORMAT_NAME,
                        session.builtinTypes.stringType.type,
                        isVal = true,
                        hasBackingField = true
                    ).symbol
                )
            }

            else -> emptyList()
        }
    }

    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        if (! context.isAdatCompanion && ! context.isAdatClass) return emptyList()

        return when (callableId.callableName) {
            Names.NEW_INSTANCE -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, context.adatClassType).symbol,
                    createMemberFunction(context.owner, AdatPluginKey, callableId.callableName, context.adatClassType) {
                        valueParameter(Names.VALUES, nullableAnyArrayType)
                    }.symbol
                )
            }

            Names.GEN_GET_VALUE -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, session.builtinTypes.nullableAnyType.type) {
                        valueParameter(Names.INDEX, session.builtinTypes.intType.type)
                    }.symbol
                )
            }

            Names.GEN_SET_VALUE -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, session.builtinTypes.unitType.type) {
                        valueParameter(Names.INDEX, session.builtinTypes.intType.type)
                        valueParameter(Names.VALUE, session.builtinTypes.nullableAnyType.type)
                    }.symbol
                )
            }

            Names.EQUALS -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, session.builtinTypes.booleanType.type) {
                        valueParameter(Names.OTHER, session.builtinTypes.nullableAnyType.type)
                    }.symbol
                )
            }

            Names.HASHCODE -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, session.builtinTypes.intType.type).symbol
                )
            }

            Names.TO_STRING -> {
                listOf(
                    createMemberFunction(context !!.owner, AdatPluginKey, callableId.callableName, session.builtinTypes.stringType.type).symbol
                )
            }

            else -> emptyList()
        }
    }

    val FirClassSymbol<*>.isAdatClass
        get() = session.predicateBasedProvider.matches(ADAT_PREDICATE, this)

    val FirClassSymbol<*>.isAdatCompanion
        get() = ((origin as? FirDeclarationOrigin.Plugin)?.key == AdatPluginKey)
            || getSuperTypes(session).any { it.type.classId == ClassIds.ADAT_COMPANION }
            || getContainingClassSymbol(session)?.getSuperTypes(session)?.any { it.type.classId == ClassIds.ADAT_COMPANION } ?: false

    val MemberGenerationContext?.isAdatClass
        get() = if (this == null) false else owner.isAdatClass

    val MemberGenerationContext?.isAdatCompanion
        get() = isFromPlugin(AdatPluginKey)

    val MemberGenerationContext.adatClassType
        get() = if (isAdatClass) {
            owner.classId.constructClassLikeType(emptyArray(), false)
        } else {
            owner.getContainingClassSymbol(session) !!.classId.constructClassLikeType(emptyArray(), false)
        }

}