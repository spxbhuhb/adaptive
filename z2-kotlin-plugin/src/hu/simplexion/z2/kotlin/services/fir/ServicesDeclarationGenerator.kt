package hu.simplexion.z2.kotlin.services.fir

import hu.simplexion.z2.kotlin.services.*
import hu.simplexion.z2.kotlin.common.isFromPlugin
import hu.simplexion.z2.kotlin.common.superTypeContains
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.expandedClass
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingDeclarationSymbol
import org.jetbrains.kotlin.fir.analysis.checkers.toClassLikeSymbol
import org.jetbrains.kotlin.fir.declarations.utils.isSuspend
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
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
 * Add declarations for service interfaces (interfaces that extend `Service`).
 *
 * - a nested class with name `<service-interface-name>$Consumer`:
 *   - implements the service interface (added as supertype)
 *   - has an empty constructor
 *   - has a `serviceFqName : String` property
 *   - has overrides for all functions defined in the service interface
 */
class ServicesDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    companion object {
        // this is here in case a refactoring puts in the fully qualified name, it happens sometimes
        val SERVICES_FQ = Strings.SERVICES_PACKAGE + "." + Strings.SERVICE
    }

    val serviceCallTransportType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.SERVICE_CALL_TRANSPORT) !!.constructType(emptyArray(), true)
    }

    @OptIn(SymbolInternals::class)
    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {
        if (classSymbol.fir.classKind != ClassKind.INTERFACE) return emptySet()

        if (! classSymbol.superTypeContains(Strings.SERVICE, SERVICES_FQ)) {
            return emptySet()
        }

        return setOf(classSymbol.name.serviceConsumerName)
    }

    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {

        if (name != owner.name.serviceConsumerName) return null

        val firClass = createNestedClass(owner, name, ServicesPluginKey) {
            superType(owner.defaultType())
        }

        return firClass.symbol
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
        val expandedClass = classLikeSymbol.expandedClass(session) !!

        val symbols = expandedClass
            .declarationSymbols
            .filterIsInstance<FirNamedFunctionSymbol>()
            .filter { it.isSuspend }
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