package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.internal.backend.BackendBase

abstract class FrontendBase : AdatStore() {

    abstract val backend: BackendBase

    abstract fun commit()

}