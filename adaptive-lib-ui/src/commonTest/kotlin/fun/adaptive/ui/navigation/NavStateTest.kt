package `fun`.adaptive.ui.navigation

import kotlin.test.Test
import kotlin.test.assertEquals


class NavStateTest {

    @Test
    fun testToUrl_noParameters_noTag_noCustom() {
        val navState = NavState(listOf("segment1", "segment2"))
        val expectedUrl = "/segment1/segment2"
        assertEquals(expectedUrl, navState.toUrl())
    }

    @Test
    fun testToUrl_withParameters_noTag_noCustom() {
        val navState = NavState(
            segments = listOf("segment1", "segment2"),
            parameters = mapOf("param1" to "value1", "param2" to "value2")
        )
        val expectedUrl = "/segment1/segment2?param1=value1&param2=value2"
        assertEquals(expectedUrl, navState.toUrl())
    }

    @Test
    fun testToUrl_withParameters_withTag_noCustom() {
        val navState = NavState(
            segments = listOf("segment1", "segment2"),
            parameters = mapOf("param1" to "value1", "param2" to "value2"),
            tag = "myTag"
        )
        val expectedUrl = "/segment1/segment2?param1=value1&param2=value2#myTag"
        assertEquals(expectedUrl, navState.toUrl())
    }

    @Test
    fun testToUrl_withParameters_withTag_withCustom() {
        val navState = NavState(
            segments = listOf("segment1", "segment2"),
            parameters = mapOf("param1" to "value1", "param2" to "value2"),
            tag = "myTag",
            custom = "customPart"
        )
        val expectedUrl = "/segment1/segment2?param1=value1&param2=value2#myTag|customPart"
        assertEquals(expectedUrl, navState.toUrl())
    }

    @Test
    fun testToUrl_withEncodedCharacters() {
        val navState = NavState(
            segments = listOf("this is a test"),
            parameters = mapOf("key" to "value with spaces"),
            tag = "tag with spaces",
            custom = "custom with spaces"
        )
        val expectedUrl = "/this+is+a+test?key=value+with+spaces#tag+with+spaces|custom+with+spaces"
        assertEquals(expectedUrl, navState.toUrl())
    }

    @Test
    fun testParse_noParameters_noTag_noCustom() {
        val url = "/segment1/segment2"
        val navState = NavState.parse(url)
        val expectedNavState = NavState(listOf("segment1", "segment2"))
        assertEquals(expectedNavState.toUrl(), navState.toUrl())
    }

    @Test
    fun testParse_withParameters_noTag_noCustom() {
        val url = "/segment1/segment2?param1=value1&param2=value2"
        val navState = NavState.parse(url)
        val expectedNavState = NavState(
            segments = listOf("segment1", "segment2"),
            parameters = mapOf("param1" to "value1", "param2" to "value2")
        )
        assertEquals(expectedNavState.toUrl(), navState.toUrl())
    }

    @Test
    fun testParse_withParameters_withTag_noCustom() {
        val url = "/segment1/segment2?param1=value1&param2=value2#myTag"
        val navState = NavState.parse(url)
        val expectedNavState = NavState(
            segments = listOf("segment1", "segment2"),
            parameters = mapOf("param1" to "value1", "param2" to "value2"),
            tag = "myTag"
        )
        assertEquals(expectedNavState.toUrl(), navState.toUrl())
    }

    @Test
    fun testParse_withParameters_withTag_withCustom() {
        val url = "/segment1/segment2?param1=value1&param2=value2#myTag|customPart"
        val navState = NavState.parse(url)
        val expectedNavState = NavState(
            segments = listOf("segment1", "segment2"),
            parameters = mapOf("param1" to "value1", "param2" to "value2"),
            tag = "myTag",
            custom = "customPart"
        )
        assertEquals(expectedNavState.toUrl(), navState.toUrl())
    }

    @Test
    fun testParse_withEncodedCharacters() {
        val url = "/this+is+a+test?key=value+with+spaces#tag+with+spaces|custom+with+spaces"
        val navState = NavState.parse(url)
        val expectedNavState = NavState(
            segments = listOf("this is a test"),
            parameters = mapOf("key" to "value with spaces"),
            tag = "tag with spaces",
            custom = "custom with spaces"
        )
        assertEquals(expectedNavState.toUrl(), navState.toUrl())
    }

    @Test
    fun testParse_withProtocolHostAndPort() {
        val urlHttp = "http://example.com:8080/segment1/segment2?param1=value1&param2=value2#myTag|customPart"
        val navStateHttp = NavState.parse(urlHttp)
        val expectedNavStateHttp = NavState(
            segments = listOf("segment1", "segment2"),
            parameters = mapOf("param1" to "value1", "param2" to "value2"),
            tag = "myTag",
            custom = "customPart"
        )
        assertEquals(expectedNavStateHttp.toUrl(), navStateHttp.toUrl())

        val urlHttps = "https://example.com/segment1/segment2?param1=value1&param2=value2#myTag|customPart"
        val navStateHttps = NavState.parse(urlHttps)
        val expectedNavStateHttps = NavState(
            segments = listOf("segment1", "segment2"),
            parameters = mapOf("param1" to "value1", "param2" to "value2"),
            tag = "myTag",
            custom = "customPart"
        )
        assertEquals(expectedNavStateHttps.toUrl(), navStateHttps.toUrl())
    }

}