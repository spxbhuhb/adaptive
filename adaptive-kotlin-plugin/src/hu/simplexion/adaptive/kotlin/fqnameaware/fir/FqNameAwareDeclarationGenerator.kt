/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.fqnameaware.fir

import hu.simplexion.adaptive.kotlin.fqnameaware.ClassIds
import hu.simplexion.adaptive.kotlin.fqnameaware.FqNameAwarePluginKey
import hu.simplexion.adaptive.kotlin.fqnameaware.Names
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.plugin.createMemberProperty
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirPropertySymbol
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.constructType
import org.jetbrains.kotlin.fir.types.isSubtypeOf
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.Name

class FqNameAwareDeclarationGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {

    val fqNameAwareType by lazy {
        session.symbolProvider.getClassLikeSymbolByClassId(ClassIds.FQ_NAME_AWARE) !!.constructType(emptyArray(), true)
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {

        val fqNameAware = (classSymbol.resolvedSuperTypeRefs.firstOrNull { it.type.isSubtypeOf(fqNameAwareType, session) } != null)

        return if (fqNameAware && classSymbol.classKind != ClassKind.INTERFACE) {
            setOf(Names.CLASS_FQ_NAME)
        } else {
            emptySet()
        }
    }

    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        return when (callableId.callableName) {
            Names.CLASS_FQ_NAME -> {
                listOf(
                    createMemberProperty(
                        context !!.owner,
                        FqNameAwarePluginKey,
                        Names.CLASS_FQ_NAME,
                        session.builtinTypes.stringType.coneType,
                        isVal = true,
                        hasBackingField = false
                    ).symbol
                )
            }
            else -> emptyList()
        }
    }
}