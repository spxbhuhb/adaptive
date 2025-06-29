package `fun`.adaptive.ui.navigation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.lib.util.url.Url

fun navState(vararg segments: String, title: String? = null, fullScreen: Boolean = false) =
    NavState(Url(segments = segments.toList()), title = title, fullScreen = fullScreen)

//fun NavStateOrigin.open(navState: NavState) {
//    navState.update(
//        navState::segments,
//        navState::parameters,
//        navState::tag,
//        navState::custom,
//        navState::title,
//        navState::fullScreen
//    )
//}

@Adat
open class NavState(
    val url: Url,
    val title: String? = null,
    val fullScreen: Boolean = false,
) {

    /**
     * Create a [NavState] which is a sub-state of this one by adding the segments
     * to the segments of this.
     */
    fun sub(vararg segments: String): NavState =
        NavState(
            this.url.copy(segments = this.url.segments + segments),
            this.title,
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
        if (other.url.segments.size < this.url.segments.size) return false

        for (i in other.url.segments.indices) {
            if (i > this.url.segments.lastIndex) return true
            if (other.url.segments[i] != this.url.segments[i]) return false
        }

        return true
    }

    fun goto(newState: NavState) {
        store().update(newState)
    }

    private fun store(): AdatStore<NavState> {
        @Suppress("UNCHECKED_CAST")
        val store = adatContext?.store as? AdatStore<NavState>
        return checkNotNull(store) { "no store for the nav state" }
    }

    companion object : AdatCompanion<NavState> {

        fun parse(url: String): NavState {
            return NavState(Url.parse(url))
        }
    }
}