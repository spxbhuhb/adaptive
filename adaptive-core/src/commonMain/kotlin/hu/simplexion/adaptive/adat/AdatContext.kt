package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.descriptor.result.InstanceValidationResult
import hu.simplexion.adaptive.adat.store.AdatStore

class AdatContext(
    var store: AdatStore? = null,
    var validationResult: InstanceValidationResult? = null
)