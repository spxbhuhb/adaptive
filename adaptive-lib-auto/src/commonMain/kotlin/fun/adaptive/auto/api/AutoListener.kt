package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass

abstract class AutoListener<A : AdatClass> {

    open fun onAdd(item : A) {

    }

    open fun onRemove(item : A) {

    }

    open fun onListCommit(newValue: List<A>) {

    }

    open fun onItemCommit(item: A) {

    }

}