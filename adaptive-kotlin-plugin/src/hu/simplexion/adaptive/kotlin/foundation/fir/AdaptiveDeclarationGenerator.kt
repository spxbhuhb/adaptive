/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.fir

import hu.simplexion.adaptive.kotlin.foundation.ClassIds
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.utils.visibility
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId

// @OptIn(ExperimentalTopLevelDeclarationsGenerationApi::class) // uncomment for 2.0.0-RC2
class AdaptiveDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    companion object {
        // TODO verify that collecting functions in FIR for companion collector is correct
        //
        // https://kotlinlang.slack.com/archives/C7L3JB43G/p1704374619907359
        //
        // Q: Would it be safe to pass data between the FIR and backend IR parts of a plugin? Are there any considerations to pay attention to? My plugin builds a representation of the code and it would be a waste to build it twice.
        // A: It's safe if you can guarantee that you definitely collect all the information before reading it at the backend
        //
        // However, I don't know if collecting it with the predicate provides that guarantee or not.

        val adaptiveFunctions = mutableSetOf<CallableId>()

    }

    val ADAPTIVE_PREDICATE = LookupPredicate.create { annotated(ClassIds.ADAPTIVE.asSingleFqName()) }
    val ADAPTIVE_EXPECT_PREDICATE = LookupPredicate.create { annotated(ClassIds.ADAPTIVE_EXPECT.asSingleFqName()) }

    private val predicateBasedProvider = session.predicateBasedProvider

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(ADAPTIVE_PREDICATE)
        register(ADAPTIVE_EXPECT_PREDICATE)
    }

    override fun getTopLevelClassIds(): Set<ClassId> {

        adaptiveFunctions.addAll(
            predicateBasedProvider
                .getSymbolsByPredicate(ADAPTIVE_PREDICATE)
                .filterNot { predicateBasedProvider.matches(ADAPTIVE_EXPECT_PREDICATE, it) }
                .filterIsInstance<FirNamedFunctionSymbol>()
                .map { it.callableId }
        )

        return emptySet()
    }

}
