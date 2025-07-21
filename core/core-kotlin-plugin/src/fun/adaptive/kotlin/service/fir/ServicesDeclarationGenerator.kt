/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.service.fir

import `fun`.adaptive.kotlin.adat.AdatPluginKey
import `fun`.adaptive.kotlin.common.isFromPlugin
import `fun`.adaptive.kotlin.service.*
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingClassSymbol
import org.jetbrains.kotlin.fir.analysis.checkers.toClassLikeSymbol
import org.jetbrains.kotlin.fir.declarations.DirectDeclarationsAccess
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
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
import org.jetbrains.kotlin.fir.resolve.getSuperTypes
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.scopes.FirScope
import org.jetbrains.kotlin.fir.scopes.getFunctions
import org.jetbrains.kotlin.fir.scopes.impl.declaredMemberScope
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.types.classId
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

    val serviceConsumerType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.SERVICE_CONSUMER) !!.constructType(emptyArray(), true)
    }

    val serviceCallTransportType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.SERVICE_CALL_TRANSPORT) !!.constructType(emptyArray(), true)
    }

    val serviceContextType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.SERVICE_CONTEXT) !!.constructType(emptyArray(), true)
    }

    val backendFragmentType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.BACKEND_FRAGMENT) !!.constructType(emptyArray(), true)
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

    private val implCallableNames = setOf(
        Names.SERVICE_NAME,
        Names.SERVICE_CONTEXT_PROPERTY,
        Names.SERVICE_CALL_TRANSPORT_PROPERTY,
        Names.FRAGMENT,
        SpecialNames.INIT
    )

    @OptIn(DirectDeclarationsAccess::class)
    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        val names = classSymbol.declarationSymbols.mapNotNull { if (it is FirNamedFunctionSymbol && ! it.name.isSpecial) it.name else null }

        val callableNames = when {
            classSymbol.isServiceApi -> consumerCallableNames - names + collectFunctions(classSymbol.getContainingClassSymbol() !!.classId)
            classSymbol.isServiceImpl -> implCallableNames - names
            else -> emptySet()
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
        return listOf(
            createConstructor(context.owner, ServicesPluginKey, isPrimary = true, generateDelegatedNoArgConstructorCall = true).symbol
        )
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        if (context.isForeign) return emptyList()

        return when (callableId.callableName) {
            Names.FRAGMENT -> {
                listOf(
                    createMemberProperty(
                        context !!.owner,
                        ServicesPluginKey,
                        Names.FRAGMENT,
                        backendFragmentType,
                        isVal = false,
                        hasBackingField = true
                    ).symbol
                )
            }

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

    @OptIn(SymbolInternals::class)
    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        if (context.isForeign) return emptyList()
        requireNotNull(context)

        val functionName = callableId.callableName

        when (functionName) {
            Names.NEW_INSTANCE -> {
                listOf(
                    createMemberFunction(context.owner, AdatPluginKey, callableId.callableName, context.owner.defaultType()) {
                        valueParameter(Names.SERVICE_CONTEXT_PARAMETER, serviceContextType)
                    }.symbol
                )
            }
        }

        val interfaceFunctions = context.owner.resolvedSuperTypeRefs
            .map { session.getClassDeclaredFunctionSymbols(it.toClassLikeSymbol(session) !!.classId, functionName) }
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

    val MemberGenerationContext?.isForeign
        get() = ! isFromPlugin(ServicesPluginKey)

    val FirClassSymbol<*>.isServiceApi
        get() = session.predicateBasedProvider.matches(SERVICE_API_PREDICATE, this)

    val FirClassSymbol<*>.isServiceImpl
        get() = getSuperTypes(session).any { it.classId == ClassIds.SERVICE_IMPL }
}