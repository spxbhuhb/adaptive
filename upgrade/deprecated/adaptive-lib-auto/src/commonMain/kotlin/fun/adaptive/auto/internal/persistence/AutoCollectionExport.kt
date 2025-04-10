package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.auto.model.AutoMetadata

class AutoCollectionExport<IT>(
    override val meta: AutoMetadata?,
    val items : Collection<AutoItemExport<IT>>?
) : AutoExport<Collection<IT>>()