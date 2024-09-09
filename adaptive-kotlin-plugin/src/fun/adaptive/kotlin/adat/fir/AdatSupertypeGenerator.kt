package `fun`.adaptive.kotlin.adat.fir

import `fun`.adaptive.kotlin.adat.ClassIds
import `fun`.adaptive.kotlin.adat.FqNames
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirClassLikeDeclaration
import org.jetbrains.kotlin.fir.declarations.FirRegularClass
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.FirSupertypeGenerationExtension
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.types.FirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.constructClassLikeType
import org.jetbrains.kotlin.name.ClassId

class AdatSupertypeGenerator(session: FirSession) : FirSupertypeGenerationExtension(session) {

    companion object {
        val companions = mutableSetOf<ClassId>()
    }

    val ADAT_PREDICATE = LookupPredicate.create { annotated(FqNames.ADAT_ANNOTATION) }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(ADAT_PREDICATE)
    }

    override fun needTransformSupertypes(declaration: FirClassLikeDeclaration): Boolean {
        val matches = session.predicateBasedProvider.matches(ADAT_PREDICATE, declaration.symbol)
        if (matches && declaration is FirRegularClass && declaration.companionObjectSymbol != null) {
            companions += declaration.companionObjectSymbol !!.classId
        }
        val companion = declaration.symbol.classId in companions
        return (matches || companion)
    }

    override fun computeAdditionalSupertypes(
        classLikeDeclaration: FirClassLikeDeclaration,
        resolvedSupertypes: List<FirResolvedTypeRef>,
        typeResolver: TypeResolveService
    ): List<FirResolvedTypeRef> {

        val symbol = classLikeDeclaration.symbol

        if (symbol.classId in companions) {
            if (resolvedSupertypes.any { it.type.classId == ClassIds.ADAT_COMPANION }) return emptyList()

            val adatClassType = symbol.classId.constructClassLikeType(emptyArray(), false)

            return listOf(
                buildResolvedTypeRef {
                    type = ClassIds.ADAT_COMPANION.constructClassLikeType(arrayOf(adatClassType), false)
                }
            )

        } else {
            if (resolvedSupertypes.any { it.type.classId == ClassIds.ADAT_CLASS || it.type.classId == ClassIds.ADAT_ENTITY }) return emptyList()

            // FIXME AdatEntity handling

            return listOf(
                buildResolvedTypeRef {
                    type = ClassIds.ADAT_CLASS.constructClassLikeType(emptyArray(), false)
                }
            )
        }
    }

}