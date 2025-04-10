package `fun`.adaptive.model

import `fun`.adaptive.adat.AdatClass

abstract class NamedItem : AdatClass {
    abstract val name: String
    abstract val type: NamedItemType
}