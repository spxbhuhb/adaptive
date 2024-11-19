package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.auto.model.AutoMetadata

abstract class AutoExport<VT> {
    abstract val meta: AutoMetadata<VT>?
}