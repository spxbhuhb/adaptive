package `fun`.adaptive.utility

class Url(
    val scheme: String = "",
    val segments: List<String> = emptyList(),
    val parameters: Map<String, String> = emptyMap(),
    val tag: String = "",
    val custom: String = "",
) {

    companion object {
        private val regex = Regex("(?:([a-z]+)://)?(?:[a-zA-Z0-9.-]+(?::[0-9]{1,5})?)?([^?#]*)(\\?[^#]*)?(#[^|]*)?(\\|.*)?")

        fun String.parseUrl(): Url = parse(this)

        fun parse(url: String): Url {
            try {
                val match = regex.matchEntire(url)
                requireNotNull(match)

                val scheme = match.groupValues[1]

                val path = match
                    .groupValues[2]
                    .split('/')
                    .mapNotNull { it.decodeFromUrl().ifEmpty { null } }

                val parameters = match
                    .groupValues[3]
                    .removePrefix("?")
                    .split('&')
                    .mapNotNull { it.decodeFromUrl().ifEmpty { null } }
                    .map { it.split('=', limit = 2) }
                    .associate { it[0].decodeFromUrl() to it[1].decodeFromUrl() }

                val tag = match
                    .groupValues[4]
                    .removePrefix("#")
                    .decodeFromUrl()

                val custom = match
                    .groupValues[5]
                    .removePrefix("|")
                    .decodeFromUrl()

                return Url(scheme, path, parameters, tag, custom)
            } catch (ex: Exception) {
                throw IllegalArgumentException("unable to parse URL: $url", ex)
            }
        }
    }
}