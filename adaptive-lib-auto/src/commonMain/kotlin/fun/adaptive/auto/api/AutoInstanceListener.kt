package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass

abstract class AutoInstanceListener<A : AdatClass> {

    open fun onChange(newValue: A, oldValue : A?) = Unit

}