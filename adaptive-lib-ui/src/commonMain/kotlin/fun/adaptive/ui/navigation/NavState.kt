package `fun`.adaptive.ui.navigation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.api.ItemBase

typealias NavStateOrigin = ItemBase<NavState>

fun NavStateOrigin.open(navState: NavState) {
    update(
        navState::segments,
        navState::parameters,
        navState::tag,
        navState::custom,
        navState::fullScreen
    )
}

@Adat
open class NavState(
    val segments: List<String> = emptyList(),
    val parameters: Map<String, String> = emptyMap(),
    val tag: String = "",
    val custom: String = "",
    val fullScreen: Boolean = false,
) {

    constructor(path: String) : this(segments = path.split("/").mapNotNull { it.ifEmpty { null } })

    /**
     * Create a [NavState] which is a sub-state of this one by adding the segments
     * to the segments of this.
     */
    fun sub(vararg segments : String) : NavState =
        NavState(
            this.segments + segments,
            this.parameters,
            this.tag,
            this.custom,
            this.fullScreen
        )

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
            if (i > this.segments.lastIndex) return true
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