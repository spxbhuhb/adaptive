/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.service.fir

import `fun`.adaptive.kotlin.common.isFromPlugin
import `fun`.adaptive.kotlin.service.*
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingClassSymbol
import org.jetbrains.kotlin.fir.analysis.checkers.toClassLikeSymbol
import org.jetbrains.kotlin.fir.declarations.DirectDeclarationsAccess
import org.jetbrains.kotlin.fir.declarations.fullyExpandedClass
import org.jetbrains.kotlin.fir.declarations.utils.isSuspend
import org.jetbrains.kotlin.fir.extensions.*
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.plugin.createConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createMemberProperty
import org.jetbrains.kotlin.fir.plugin.createNestedClass
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.scopes.FirScope
import org.jetbrains.kotlin.fir.scopes.getFunctions
import org.jetbrains.kotlin.fir.scopes.impl.declaredMemberScope
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.types.ConeStarProjection
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
    val SERVICE_PROVIDER_PREDICATE = LookupPredicate.create { annotated(FqNames.SERVICE_PROVIDER) }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(SERVICE_API_PREDICATE)
        register(SERVICE_PROVIDER_PREDICATE)
    }

    val serviceConsumerType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.SERVICE_CONSUMER) !!.constructType(emptyArray(), true)
    }

    val serviceCallTransportType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.SERVICE_CALL_TRANSPORT) !!.constructType(emptyArray(), true)
    }

    val serviceContextType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.SERVICE_CONTEXT) !!.constructType(emptyArray(), false)
    }

    val backendFragmentType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.BACKEND_FRAGMENT) !!.constructType(emptyArray(), true)
    }

    val byteArrayType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.KOTLIN_BYTEARRAY) !!.constructType(emptyArray(), false)
    }

    val wireFormatDecoderType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.WIREFORMAT_DECODER) !!.constructType(arrayOf(ConeStarProjection), false)
    }

    val adaptiveLoggerType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.ADAPTIVE_LOGGER) !!.constructType(emptyArray(), false)
    }


    @OptIn(SymbolInternals::class)
    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {
        if (classSymbol.fir.classKind != ClassKind.INTERFACE) return emptySet()
        if (! session.predicateBasedProvider.matches(SERVICE_API_PREDICATE, classSymbol)) return emptySet()
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

    private val consumerCallableNames = setOf(
        Names.SERVICE_NAME,
        Names.SERVICE_CALL_TRANSPORT_PROPERTY,
        SpecialNames.INIT
    )

    // provides uses transport from context, so we don't have to override it
    private val providerCallableNames = setOf(
        Names.SERVICE_NAME,
        Names.NEW_INSTANCE,
        Names.DISPATCH,
        Names.LOGGER
    )

    @OptIn(DirectDeclarationsAccess::class)
    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        val names = classSymbol.declarationSymbols.mapNotNull { if (it is FirNamedFunctionSymbol && ! it.name.isSpecial) it.name else null }

        val callableNames = when {
            classSymbol.isServiceApi -> emptySet()
            classSymbol.isServiceProvider -> providerCallableNames - names
            context.isForeign -> emptySet()
            // this is the consumer class
            else -> consumerCallableNames - names + collectFunctions(classSymbol.getContainingClassSymbol() !!.classId)
        }

        return callableNames
    }

    private fun collectFunctions(classId: ClassId) =
        collectFunctions(session.symbolProvider.getClassLikeSymbolByClassId(classId) !!)

    @OptIn(DirectDeclarationsAccess::class)
    private fun collectFunctions(classLikeSymbol: FirClassLikeSymbol<*>): Set<Name> {
        val expandedClass = classLikeSymbol.fullyExpandedClass(session) !!

        val symbols = expandedClass
            .declarationSymbols
            .filterIsInstance<FirNamedFunctionSymbol>()
            .filter { it.isSuspend && ! it.name.isSpecial && it.name.identifier != Strings.CALL_SERVICE }
            .map { it.name }
            .toMutableSet()

        expandedClass.resolvedSuperTypeRefs.forEach {
            symbols += collectFunctions(it.toClassLikeSymbol(session) !!)
        }

        return symbols
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        // consumer only
        return listOf(
            createConstructor(context.owner, ServicesPluginKey, isPrimary = true, generateDelegatedNoArgConstructorCall = true).symbol
        )
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        if (! context.isServiceProvider && context.isForeign) return emptyList()

        return when (callableId.callableName) {
            // consumer and provider
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

            // consumer and provider
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

            // provider
            Names.LOGGER -> {
                listOf(
                    createMemberProperty(
                        context !!.owner,
                        ServicesPluginKey,
                        Names.LOGGER,
                        adaptiveLoggerType,
                        isVal = false,
                        hasBackingField = true
                    ).symbol
                )
            }

            else -> emptyList()
        }
    }

    @OptIn(SymbolInternals::class)
    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        requireNotNull(context)

        val functionName = callableId.callableName

        if (context.isServiceProvider) {
            when (functionName) {
                Names.NEW_INSTANCE -> {
                    createMemberFunction(context.owner, ServicesPluginKey, callableId.callableName, context.owner.defaultType()) {
                        valueParameter(Names.SERVICE_CONTEXT_PARAMETER, serviceContextType)
                    }.symbol
                }

                Names.DISPATCH -> {
                    createMemberFunction(context.owner, ServicesPluginKey, callableId.callableName, byteArrayType) {
                        //funName: String, payload: WireFormatDecoder<*>
                        valueParameter(Names.FUN_NAME, session.builtinTypes.stringType.coneType)
                        valueParameter(Names.PAYLOAD, wireFormatDecoderType)
                        status {
                            isSuspend = true
                        }
                    }.symbol
                }

                else -> null
            }.also {
                return if (it != null) listOf(it) else emptyList()
            }
        }

        // to skip the service API interface
        if (context.isForeign) return emptyList()

        val interfaceFunctions = context.owner.resolvedSuperTypeRefs
            .map { session.getClassDeclaredFunctionSymbols(it.toClassLikeSymbol(session) !!.classId, functionName) }
            .flatten()
            .filter { it.name.identifier != Strings.CALL_SERVICE }

        interfaceFunctions.map { interfaceFunction ->
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
                interfaceFunction.typeParameterSymbols.forEach { typeParameterSymbol ->
                    typeParameter(
                        typeParameterSymbol.name,
                        typeParameterSymbol.variance,
                        isReified = typeParameterSymbol.isReified
                    ) {
                        bound(typeParameterSymbol.fir.bounds.first().coneType)
                    }
                }
            }.symbol
        }.also {
            return it
        }
    }

    @OptIn(SymbolInternals::class)
    private fun FirSession.getClassDeclaredMemberScope(classId: ClassId): FirScope? {
        val classSymbol = symbolProvider.getClassLikeSymbolByClassId(classId) as? FirRegularClassSymbol ?: return null
        return declaredMemberScope(classSymbol.fir, memberRequiredPhase = null)
    }

    fun FirSession.getClassDeclaredFunctionSymbols(classId: ClassId, name: Name): List<FirFunctionSymbol<*>> {
        val classMemberScope = getClassDeclaredMemberScope(classId)
        return classMemberScope?.getFunctions(name).orEmpty()
    }

    val FirClassSymbol<*>.isServiceApi
        get() = session.predicateBasedProvider.matches(SERVICE_API_PREDICATE, this)

    val FirClassSymbol<*>.isServiceProvider
        get() = session.predicateBasedProvider.matches(SERVICE_PROVIDER_PREDICATE, this)

    val MemberGenerationContext?.isForeign
        get() = ! isFromPlugin(ServicesPluginKey)

    val MemberGenerationContext?.isServiceProvider
        get() = this?.owner?.isServiceProvider == true

}