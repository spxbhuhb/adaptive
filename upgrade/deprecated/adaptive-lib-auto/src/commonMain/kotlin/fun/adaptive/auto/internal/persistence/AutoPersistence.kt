package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass

abstract class AutoPersistence<VT,IT : AdatClass> {

    abstract fun load(): AutoExport<VT>?

}