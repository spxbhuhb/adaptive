package `fun`.adaptive.utility

import kotlin.test.*

class UrlTest {

    @Test
    fun `parse URL with full components`() {
        val urlString = "http://host/path/to/resource?key1=value1&key2=value2#tag|custom"
        val url = Url.parse(urlString)

        assertEquals("http", url.scheme)
        assertEquals(listOf("host", "path", "to", "resource"), url.segments)
        assertEquals(mapOf("key1" to "value1", "key2" to "value2"), url.parameters)
        assertEquals("tag", url.tag)
        assertEquals("custom", url.custom)
    }

    @Test
    fun `parse URL with missing optional components`() {
        val urlString = "ftp://host/resource"
        val url = Url.parse(urlString)

        assertEquals("ftp", url.scheme)
        assertEquals(listOf("host", "resource"), url.segments)
        assertTrue(url.parameters.isEmpty())
        assertEquals("", url.tag)
        assertEquals("", url.custom)
    }

    @Test
    fun `parse URL with encoded components`() {
        val urlString = "https://host/path%20one?key%201=value%202#some%20tag|custom%20data"
        val url = Url.parse(urlString)

        assertEquals("https", url.scheme)
        assertEquals(listOf("host", "path one"), url.segments)
        assertEquals(mapOf("key 1" to "value 2"), url.parameters)
        assertEquals("some tag", url.tag)
        assertEquals("custom data", url.custom)
    }

    @Test
    fun `parse URL with empty query parameters`() {
        val urlString = "scheme://host/path?#|"
        val url = Url.parse(urlString)

        assertEquals("scheme", url.scheme)
        assertEquals(listOf("host", "path"), url.segments)
        assertTrue(url.parameters.isEmpty())
        assertEquals("", url.tag)
        assertEquals("", url.custom)
    }

    @Test
    fun `parse URL with no scheme and minimal parts`() {
        val urlString = "/a/b"
        val url = Url.parse(urlString)

        assertEquals("", url.scheme)
        assertEquals(listOf("", "a", "b"), url.segments)
        assertTrue(url.parameters.isEmpty())
        assertEquals("", url.tag)
        assertEquals("", url.custom)
    }

    @Test
    fun `parse URL with complex query and no tag or custom`() {
        val urlString = "custom://domain/path/to?x=1&y=2"
        val url = Url.parse(urlString)

        assertEquals("custom", url.scheme)
        assertEquals(listOf("domain", "path", "to"), url.segments)
        assertEquals(mapOf("x" to "1", "y" to "2"), url.parameters)
        assertEquals("", url.tag)
        assertEquals("", url.custom)
    }

    @Test
    fun `reinitialization does not retain previous state`() {
        val url1 = Url.parse("scheme://host/path1#tag1|custom1")
        val url2 = Url.parse("scheme://host/path2?param=value")

        assertEquals(listOf("host", "path1"), url1.segments)
        assertEquals("tag1", url1.tag)
        assertEquals("custom1", url1.custom)

        assertEquals(listOf("host", "path2"), url2.segments)
        assertEquals(mapOf("param" to "value"), url2.parameters)
        assertEquals("", url2.tag)
        assertEquals("", url2.custom)
    }

    @Test
    fun `parse invalid URL`() {
        val urlString = "scheme://host/path??#|"

        val exception = assertFailsWith(IllegalArgumentException::class) {
            Url.parse(urlString)
        }

        assertTrue(exception.message?.contains("unable to parse URL: scheme://host/path??#|") == true)
    }

    @Test
    fun `parse actualize`() {
        val url = Url.parse("actualize:///cookbook/example/select-input-text")
        assertEquals("actualize", url.scheme)
        assertEquals(listOf("", "cookbook", "example", "select-input-text"), url.segments)
        assertTrue(url.parameters.isEmpty())
        assertEquals("", url.tag)
        assertEquals("", url.custom)
    }

    @Test
    fun `parse actualize with parameter`() {
        val url = Url.parse("actualize://example-group?name=intInput")
        assertEquals("actualize", url.scheme)
        assertEquals(listOf("example-group"), url.segments)
        assertTrue(url.parameters["name"] == "intInput")
        assertEquals("", url.tag)
        assertEquals("", url.custom)
    }

    @Test
    fun `toString produces correct URL string`() {
        // Test with all components
        val url1 = Url(
            scheme = "http",
            segments = listOf("host", "path", "to", "resource"),
            parameters = mapOf("key1" to "value1", "key2" to "value2"),
            tag = "tag",
            custom = "custom"
        )
        assertEquals("http://host/path/to/resource?key1=value1&key2=value2#tag|custom", url1.toString())

        // Test with missing optional components
        val url2 = Url(
            scheme = "ftp",
            segments = listOf("resource")
        )
        assertEquals("ftp://resource", url2.toString())

        // Test with components that need encoding
        val url3 = Url(
            scheme = "https",
            segments = listOf("path one"),
            parameters = mapOf("key 1" to "value 2"),
            tag = "some tag",
            custom = "custom data"
        )
        assertEquals("https://path%20one?key%201=value%202#some%20tag|custom%20data", url3.toString())

        // Test with no scheme and minimal parts
        val url4 = Url(
            segments = listOf("a", "b")
        )
        assertEquals("a/b", url4.toString())
    }

    @Test
    fun `parse and toString are inverse operations`() {
        val originalUrls = listOf(
            "http://host/path/to/resource?key1=value1&key2=value2#tag|custom",
            "ftp://host/resource",
            "https://host/path%20one?key%201=value%202#some%20tag|custom%20data",
            "/a/b",
            "custom://domain/path/to?x=1&y=2",
            "actualize://example-group?name=intInput"
        )

        for (originalUrl in originalUrls) {
            val url = Url.parse(originalUrl)
            val regeneratedUrl = url.toString()
            val reparsedUrl = Url.parse(regeneratedUrl)

            // Check that parsing and then toString produces equivalent URLs
            assertEquals(url.scheme, reparsedUrl.scheme)
            assertEquals(url.segments, reparsedUrl.segments)
            assertEquals(url.parameters, reparsedUrl.parameters)
            assertEquals(url.tag, reparsedUrl.tag)
            assertEquals(url.custom, reparsedUrl.custom)
        }
    }
}
