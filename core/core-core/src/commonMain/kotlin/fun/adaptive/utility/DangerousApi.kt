package `fun`.adaptive.utility

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is dangerous and should be used with caution."
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class DangerousApi(
    val reason: String
)