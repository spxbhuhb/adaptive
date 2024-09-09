package `fun`.adaptive.adat.store

import `fun`.adaptive.adat.AdatClass

abstract class AdatStore {

    open fun update(instance: AdatClass, path: Array<String>, value: Any?) {
        throw UnsupportedOperationException()
    }

}