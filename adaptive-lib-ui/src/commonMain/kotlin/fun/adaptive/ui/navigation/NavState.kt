package `fun`.adaptive.ui.navigation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.internal.origin.AutoInstance

typealias NavStateOrigin = AutoInstance<*, *, NavState, NavState>

fun NavStateOrigin.open(navState: NavState) {
    frontend.update(navState)
}

@Adat
open class NavState(
    val segments: List<String> = emptyList(),
    val parameters: Map<String, String> = emptyMap(),
    val tag: String = "",
    val custom: String = "",
) {

    constructor(path: String) : this(segments = path.split("/").mapNotNull { it.ifEmpty { null } })

    /**
     * A navigation state contains another when it is an "outer" state.
     *
     * For example: `/a` contains `/a/b`
     *
     * Does not check [parameters] and [tag].
     */
    operator fun contains(other: NavState?): Boolean {
        if (other == null) return false

        // this       other
        // /a/b/c     /a        as there are fewer fragments in other, it cannot contain this
        if (other.segments.size < segments.size) return false

        for (i in other.segments.indices) {
            if (other.segments[i] != segments[i]) return false
        }

        return true
    }

    fun enter(segment: Any) {
        goto(
            NavState(
                this.segments + segment.toString(),
                this.parameters,
                this.tag,
                this.custom
            )
        )
    }

    fun goto(newState: NavState) {
        store().update(newState)
    }

    fun goto(url: String) {
        store().update(parse(url))
    }

    private fun store(): AdatStore<NavState> {
        @Suppress("UNCHECKED_CAST")
        val store = adatContext?.store as? AdatStore<NavState>
        return checkNotNull(store) { "no store for the nav state" }
    }

    companion object : AdatCompanion<NavState> {
        private val regex = Regex("([^?#]*)(\\?[^#]*)?(#.*)?")

        fun parse(url: String): NavState {
            val match = regex.matchEntire(url)
            requireNotNull(match)

            val path = match
                .groupValues[1]
                .split('/')
                .mapNotNull { it.ifEmpty { null } }

            val parameters = match
                .groupValues[2]
                .split('&')
                .mapNotNull { it.ifEmpty { null } }
                .map { it.split('=', limit = 2) }
                .associate { it[0] to it[1] }

            val tag = match
                .groupValues[3]

            return NavState(path, parameters, tag)
        }
    }
}