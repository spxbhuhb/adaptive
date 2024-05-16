/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.fir

import hu.simplexion.adaptive.kotlin.foundation.FoundationPluginKey
import hu.simplexion.adaptive.kotlin.foundation.ClassIds
import hu.simplexion.adaptive.kotlin.foundation.Names
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.util.capitalizeFirstChar
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.*
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.plugin.createTopLevelFunction
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.types.ConeStarProjection
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.Name

// @OptIn(ExperimentalTopLevelDeclarationsGenerationApi::class) // uncomment for 2.0.0-RC2
class AdaptiveDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    val adapterType = ClassIds.ADAPTIVE_ADAPTER.constructClassLikeType(arrayOf(ConeStarProjection), false)
    val fragmentType = ClassIds.ADAPTIVE_FRAGMENT.constructClassLikeType(arrayOf(ConeStarProjection), false)
    val intType = session.builtinTypes.intType.coneType

    val ADAPTIVE_PREDICATE = LookupPredicate.create { annotated(ClassIds.ADAPTIVE.asSingleFqName()) }
    val ADAPTIVE_EXPECT_PREDICATE = LookupPredicate.create { annotated(ClassIds.ADAPTIVE_EXPECT.asSingleFqName()) }

    private val predicateBasedProvider = session.predicateBasedProvider

    private val matchedFunctions by lazy {
        predicateBasedProvider
            .getSymbolsByPredicate(ADAPTIVE_PREDICATE)
            .filterIsInstance<FirNamedFunctionSymbol>()
    }

    private val callableIdsForMatchedFunctions: Set<CallableId> by lazy {
        matchedFunctions.mapNotNull {
            // do not generate class function for expect fragment definitions, those are supposed to be manually coded
            if (session.predicateBasedProvider.matches(ADAPTIVE_EXPECT_PREDICATE, it)) {
                null
            } else {
                CallableId(it.callableId.packageName, Name.identifier(Strings.CLASS_FUNCTION_PREFIX + it.name.identifier.capitalizeFirstChar()))
            }
        }.toSet()
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(ADAPTIVE_PREDICATE)
        register(ADAPTIVE_EXPECT_PREDICATE)
    }

    override fun getTopLevelCallableIds(): Set<CallableId> {
        return callableIdsForMatchedFunctions
    }

    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        return listOf(
            createTopLevelFunction(
                FoundationPluginKey,
                callableId,
                fragmentType,
            ) {
                valueParameter(Names.ADAPTER, adapterType)
                valueParameter(Names.PARENT, fragmentType)
                valueParameter(Names.INDEX, intType)
            }.symbol
        )
    }

}