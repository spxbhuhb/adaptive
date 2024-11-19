package `fun`.adaptive.auto.internal.persistence

import `fun`.adaptive.adat.AdatClass

abstract class AutoCollectionPersistence<IT : AdatClass> : AutoPersistence<Collection<IT>, IT>() {

    abstract override fun load(): AutoCollectionExport<IT>

}