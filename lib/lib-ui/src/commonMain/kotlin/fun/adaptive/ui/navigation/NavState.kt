package `fun`.adaptive.ui.navigation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.utility.Url
import `fun`.adaptive.utility.encodeToUrl

fun navState(vararg segments: String, title: String? = null, fullScreen: Boolean = false) =
    NavState(segments.toList(), title = title, fullScreen = fullScreen)

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
    val segments: List<String> = emptyList(),
    val parameters: Map<String, String> = emptyMap(),
    val tag: String = "",
    val custom: String = "",
    val title: String? = null,
    val fullScreen: Boolean = false,
) {

    /**
     * Create a [NavState] which is a sub-state of this one by adding the segments
     * to the segments of this.
     */
    fun sub(vararg segments: String): NavState =
        NavState(
            this.segments + segments,
            this.parameters,
            this.tag,
            this.custom,
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
        if (other.segments.size < segments.size) return false

        for (i in other.segments.indices) {
            if (i > this.segments.lastIndex) return true
            if (other.segments[i] != segments[i]) return false
        }

        return true
    }

    fun goto(newState: NavState) {
        store().update(newState)
    }

    fun toUrl(): String {
        var result = "/" + segments.joinToString("/") { it.encodeToUrl() }
        if (parameters.isNotEmpty()) {
            result += "?" + parameters.map { "${it.key.encodeToUrl()}=${it.value.encodeToUrl()}" }.joinToString("&")
        }
        if (tag.isNotEmpty()) {
            result += "#${tag.encodeToUrl()}"
        }
        if (custom.isNotEmpty()) {
            result += "|${custom.encodeToUrl()}"
        }
        return result
    }

    private fun store(): AdatStore<NavState> {
        @Suppress("UNCHECKED_CAST")
        val store = adatContext?.store as? AdatStore<NavState>
        return checkNotNull(store) { "no store for the nav state" }
    }

    companion object : AdatCompanion<NavState> {

        fun parse(url : String): NavState {
            val url = Url.parse(url)
            return NavState(
                url.segments,
                url.parameters,
                url.tag,
                url.custom
            )
        }
    }
}