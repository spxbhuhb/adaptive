package `fun`.adaptive.lib.util.url

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.decodeFromUrl
import `fun`.adaptive.utility.encodeToUrl

/**
 * Represents an RFC 3896 URL.
 *
 * @property  segments  The segments of the URL. Made from splitting the "hier-part" of the URL
 *                      by the `/` character. This means that absolute URLs start with an
 *                      empty segment: `/a/b` results in `["","a","b"]`, while `a/b` results
 *                      in `["a","b"]`.
 */
@Adat
class Url(
    val scheme: String = "",
    val segments: List<String> = emptyList(),
    val parameters: Map<String, String> = emptyMap(),
    val tag: String = "",
    val custom: String = "",
) {

    fun segmentsStartsWith(prefix: String): Boolean {
        val segments = prefix.split('/')
        if (segments.size > segments.size) return false
        return segments.indices.all { segments[it] == segments[it] }
    }

    /**
     * Converts this [Url] to its string representation.
     * Uses [encodeToUrl] for percent encoding.
     */
    override fun toString(): String {
        val result = StringBuilder()

        // Add scheme if present
        if (scheme.isNotEmpty()) {
            result.append(scheme).append("://")
        }

        // Add path segments
        if (segments.isNotEmpty()) {
            result.append(segments.joinToString("/") { it.encodeToUrl(noPlus = true) })
        }

        // Add parameters if present
        if (parameters.isNotEmpty()) {
            result.append("?")
            result.append(parameters.entries.joinToString("&") {
                "${it.key.encodeToUrl(noPlus = true)}=${it.value.encodeToUrl(noPlus = true)}"
            })
        }

        // Add tag if present
        if (tag.isNotEmpty()) {
            result.append("#").append(tag.encodeToUrl(noPlus = true))
        }

        // Add custom if present
        if (custom.isNotEmpty()) {
            result.append("|").append(custom.encodeToUrl(noPlus = true))
        }

        return result.toString()
    }

    companion object {

        private val regex = Regex(
            "(?:([a-zA-Z][a-zA-Z0-9+\\-.]*)://)?" + // scheme
                "((?:[a-zA-Z0-9.-]+(?::[0-9]{1,5})?/)?([^?#]*))?" + // hier-part (see RFC 3986)
                "(\\?[^#]*)?" + // arguments
                "(#[^|]*)?(\\|.*)?" // tag
        )

        /**
         * Parses an RFC 3896 URL into a [Url](class://).
         */
        fun String.parseUrl(): Url = parse(this)

        /**
         * Parses [url] into a [Url](class://).
         */
        fun parse(url: String): Url {
            try {
                val match = regex.matchEntire(url)
                requireNotNull(match)

                val scheme = match.groupValues[1]

                val path = match
                    .groupValues[2]
                    .split('/')
                    .map { it.decodeFromUrl() }

                val parameters = match
                    .groupValues[4]
                    .removePrefix("?")
                    .also { require('?' !in it) }
                    .split('&')
                    .mapNotNull { it.decodeFromUrl().ifEmpty { null } }
                    .map { it.split('=', limit = 2) }
                    .associate { it[0].decodeFromUrl() to it.getOrElse(1) { "" }.decodeFromUrl() }

                val tag = match
                    .groupValues[5]
                    .removePrefix("#")
                    .decodeFromUrl()

                val custom = match
                    .groupValues[6]
                    .removePrefix("|")
                    .decodeFromUrl()

                return Url(scheme, path, parameters, tag, custom)
            } catch (ex: Exception) {
                throw IllegalArgumentException("unable to parse URL: $url", ex)
            }
        }
    }
}