package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.model.AutoConnectInfo

abstract class FrontendBase : AdatStore() {

    abstract val backend: BackendBase

    abstract fun commit()

    fun connectInfo(): AutoConnectInfo =
        backend.connectInfo()
}