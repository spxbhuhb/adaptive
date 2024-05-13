/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.service.fir

import hu.simplexion.adaptive.kotlin.common.isFromPlugin
import hu.simplexion.adaptive.kotlin.service.*
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingDeclarationSymbol
import org.jetbrains.kotlin.fir.analysis.checkers.toClassLikeSymbol
import org.jetbrains.kotlin.fir.declarations.fullyExpandedClass
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.declarations.utils.isSuspend
import org.jetbrains.kotlin.fir.extensions.*
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.plugin.createConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createMemberProperty
import org.jetbrains.kotlin.fir.plugin.createNestedClass
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.providers.getClassDeclaredFunctionSymbols
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.constructType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

/**
 * Add declarations for service interfaces (interfaces annotated with `@ServiceApi`).
 *
 * - a nested class with name `Consumer`:
 *   - implements the `ServiceConsumer` interface (added as supertype)
 *   - has an empty constructor
 *   - has a `serviceName : String` property
 *   - has overrides for all functions defined in the service interface
 */
class ServicesDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    val SERVICE_API_PREDICATE = LookupPredicate.create { annotated(FqNames.SERVICE_API) }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(SERVICE_API_PREDICATE)
    }

    private val matchedInterfaces by lazy {
        session.predicateBasedProvider
            .getSymbolsByPredicate(SERVICE_API_PREDICATE)
            .filterIsInstance<FirClassSymbol<*>>()
    }

    val serviceConsumerType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.SERVICE_CONSUMER) !!.constructType(emptyArray(), true)
    }

    val serviceCallTransportType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.SERVICE_CALL_TRANSPORT) !!.constructType(emptyArray(), true)
    }

    @OptIn(SymbolInternals::class)
    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {
        if (classSymbol.fir.classKind != ClassKind.INTERFACE) return emptySet()
        if (!session.predicateBasedProvider.matches(SERVICE_API_PREDICATE, classSymbol)) return emptySet()
        return setOf(Names.CONSUMER)
    }

    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {

        if (name != Names.CONSUMER) return null

        return createNestedClass(owner, name, ServicesPluginKey) {
            superType(owner.defaultType())
            superType(serviceConsumerType)
        }.symbol
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        if (context.isForeign) return emptySet()

        return setOf(
            Names.SERVICE_NAME,
            Names.SERVICE_CALL_TRANSPORT_PROPERTY,
            SpecialNames.INIT
        ) + collectFunctions(classSymbol.getContainingDeclarationSymbol(session) !!.classId)
    }

    private fun collectFunctions(classId: ClassId) =
        collectFunctions(session.symbolProvider.getClassLikeSymbolByClassId(classId) !!)

    private fun collectFunctions(classLikeSymbol: FirClassLikeSymbol<*>): Set<Name> {
        val expandedClass = classLikeSymbol.fullyExpandedClass(session) !!

        val symbols = expandedClass
            .declarationSymbols
            .filterIsInstance<FirNamedFunctionSymbol>()
            .filter { it.isSuspend && it.name.identifier != Strings.CALL_SERVICE }
            .map { it.name }
            .toMutableSet()

        expandedClass.resolvedSuperTypeRefs.forEach {
            symbols += collectFunctions(it.toClassLikeSymbol(session) !!)
        }

        return symbols
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        return listOf(
            createConstructor(context.owner, ServicesPluginKey, isPrimary = true, generateDelegatedNoArgConstructorCall = true).symbol
        )
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        if (context.isForeign) return emptyList()

        return when (callableId.callableName) {
            Names.SERVICE_NAME -> {
                listOf(
                    createMemberProperty(
                        context !!.owner,
                        ServicesPluginKey,
                        Names.SERVICE_NAME,
                        session.builtinTypes.stringType.coneType,
                        isVal = false,
                        hasBackingField = true
                    ).symbol
                )
            }

            Names.SERVICE_CALL_TRANSPORT_PROPERTY -> {
                listOf(
                    createMemberProperty(
                        context !!.owner,
                        ServicesPluginKey,
                        Names.SERVICE_CALL_TRANSPORT_PROPERTY,
                        serviceCallTransportType,
                        isVal = false,
                        hasBackingField = true
                    ).symbol
                )
            }

            else -> emptyList()
        }
    }

    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        if (context.isForeign) return emptyList()
        requireNotNull(context)

        val functionName = callableId.callableName

        val interfaceFunctions = context.owner.resolvedSuperTypeRefs
            .map { session.symbolProvider.getClassDeclaredFunctionSymbols(it.toClassLikeSymbol(session) !!.classId, functionName) }
            .flatten()
            .filter { it.name.identifier != Strings.CALL_SERVICE }

        return interfaceFunctions.map { interfaceFunction ->
            createMemberFunction(context.owner, ServicesPluginKey, callableId.callableName, interfaceFunction.resolvedReturnType) {
                status {
                    isSuspend = true
                }
                interfaceFunction.valueParameterSymbols.forEach { valueParameterSymbol ->
                    valueParameter(
                        valueParameterSymbol.name,
                        valueParameterSymbol.resolvedReturnType
                    )
                }
            }.symbol
        }
    }

    val MemberGenerationContext?.isForeign
        get() = ! isFromPlugin(ServicesPluginKey)
}