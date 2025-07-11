package `fun`.adaptive.utility

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.descriptor.AdatDescriptorSet
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.wireformat.fromJson

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
) : AdatClass {

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

    // --------------------------------------------------------------------------------
    // AdatClass overrides
    // --------------------------------------------------------------------------------

    override val adatCompanion: AdatCompanion<Url>
        get() = Companion

    override fun equals(other: Any?): Boolean = adatEquals(other)
    override fun hashCode(): Int = adatHashCode()

    override fun genGetValue(index: Int): Any? =
        when (index) {
            0 -> scheme
            1 -> segments
            2 -> parameters
            3 -> tag
            4 -> custom
            else -> throw IndexOutOfBoundsException()
        }
    
    fun copy(
        scheme: String = this.scheme,
        segments: List<String> = this.segments,
        parameters: Map<String, String> = this.parameters,
        tag: String = this.tag,
        custom: String = this.custom
    ): Url {
        return Url(scheme, segments, parameters, tag, custom)
    }

    companion object : AdatCompanion<Url> {

        @Suppress("UNCHECKED_CAST")
        fun decodeFromString(a: String): Url =
            a.encodeToByteArray().fromJson(this)

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

        // --------------------------------------------------------------------------------
        // AdatCompanion overrides
        // --------------------------------------------------------------------------------

        override val wireFormatName: String
            get() = "fun.adaptive.utility.Url"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = wireFormatName,
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("scheme", 0, 0, "T"),
                AdatPropertyMetadata("segments", 1, 0, "Lkotlin.collections.List<T>;"),
                AdatPropertyMetadata("parameters", 2, 0, "Lkotlin.collections.Map<TT>;"),
                AdatPropertyMetadata("tag", 3, 0, "T"),
                AdatPropertyMetadata("custom", 4, 0, "T")
            )
        )

        override val adatWireFormat: AdatClassWireFormat<Url>
            get() = AdatClassWireFormat(this, adatMetadata)

        override val adatDescriptors: Array<AdatDescriptorSet>
            get() = adatMetadata.generateDescriptors()

        override fun newInstance(values: Array<Any?>): Url {
            @Suppress("UNCHECKED_CAST")
            return Url(
                scheme = values[0] as String,
                segments = values[1] as List<String>,
                parameters = values[2] as Map<String, String>,
                tag = values[3] as String,
                custom = values[4] as String
            )
        }
    }
}
