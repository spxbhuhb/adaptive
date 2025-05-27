package `fun`.adaptive.ui.mpw.model

enum class PaneSingularity(
    val isSingular: Boolean
) {
    FULLSCREEN(true),
    SINGULAR(true),
    GROUP(false)
}