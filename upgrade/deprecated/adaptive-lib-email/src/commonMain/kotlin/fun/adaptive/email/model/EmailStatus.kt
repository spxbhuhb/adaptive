package `fun`.adaptive.email.model

enum class EmailStatus(
    val isFinal: Boolean
) {
    Preparation(false),
    SendWait(false),
    Sent(true),
    RetryWait(false),
    Failed(true)
}