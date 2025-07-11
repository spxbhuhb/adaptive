package `fun`.adaptive.ui.support

/**
 * Interface used by [AbstractParagraph](class://) to request in-application
 * navigation before using the platform default to open the URL.
 */
fun interface UiNavigator {

    /**
     * Perform navigation based on the [url] if this navigator handles
     * the given URL.
     *
     * @return  true if the navigator handled the url, false otherwise
     */
    fun onUrlTarget(url : String) : Boolean

}