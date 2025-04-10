package `fun`.adaptive.ui.workspace.model

enum class WsPaneSingularity(
    val isSingular: Boolean
) {
    FULLSCREEN(true),
    SINGULAR(true),
    GROUP(false)
}