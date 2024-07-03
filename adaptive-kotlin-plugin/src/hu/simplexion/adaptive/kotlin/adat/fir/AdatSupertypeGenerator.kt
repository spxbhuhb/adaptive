package hu.simplexion.adaptive.kotlin.adat.fir

import hu.simplexion.adaptive.kotlin.adat.ClassIds
import hu.simplexion.adaptive.kotlin.adat.FqNames
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirClassLikeDeclaration
import org.jetbrains.kotlin.fir.declarations.utils.classId
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.FirSupertypeGenerationExtension
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.types.FirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.constructClassLikeType

class AdatSupertypeGenerator(session: FirSession) : FirSupertypeGenerationExtension(session) {

    val ADAT_PREDICATE = LookupPredicate.create { annotated(FqNames.ADAT_ANNOTATION) }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(ADAT_PREDICATE)
    }

    // context(TypeResolveServiceContainer) // remove this for 2.0.0-RC2
    override fun computeAdditionalSupertypes(
        classLikeDeclaration: FirClassLikeDeclaration,
        resolvedSupertypes: List<FirResolvedTypeRef>,
        typeResolver: TypeResolveService // uncomment this for 2.0.0-RC2
    ): List<FirResolvedTypeRef> {
        if (!session.predicateBasedProvider.matches(ADAT_PREDICATE, classLikeDeclaration.symbol)) return emptyList()

        if (resolvedSupertypes.any { it.type.classId == ClassIds.ADAT_CLASS }) return emptyList()

        val adatClassType = classLikeDeclaration.classId.constructClassLikeType(emptyArray(), false)

        return listOf(
            buildResolvedTypeRef {
                type = ClassIds.ADAT_CLASS.constructClassLikeType(arrayOf(adatClassType), false)
            }
        )
    }

    override fun needTransformSupertypes(declaration: FirClassLikeDeclaration): Boolean =
        session.predicateBasedProvider.matches(ADAT_PREDICATE, declaration.symbol)

}