/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.xml

import kotlin.test.Test
import kotlin.test.assertEquals

class ParseTest {

    fun root(name: String, vararg attributes: XmlAttribute, builder: (XmlElement.() -> Unit)? = null) =
        XmlElement(
            name,
            attributes.toMutableList(),
            mutableListOf()
        ).also {
            builder?.invoke(it)
        }

    fun XmlElement.element(name: String, vararg attributes: XmlAttribute, builder: (XmlElement.() -> Unit)? = null) =
        XmlElement(
            name,
            attributes.toMutableList(),
            mutableListOf()
        ).also {
            this.children += it
            builder?.invoke(it)
        }

    fun attr(name: String, value: String) = XmlAttribute(name, value)

    fun XmlElement.content(value: String) =
        XmlContent(value).also { this.children += it }

    fun XmlElement.dump(): String =
        mutableListOf<String>().also { dump(this, "", it) }.joinToString("\n")

    fun XmlElement.dump(element: XmlElement, indentation: String = "", lines: MutableList<String>) {
        lines.add("$indentation<${element.tag}${element.attributes.joinToString(" ") { " ${it.name}=\"${it.value}\"" }}>")
        for (child in element.children) {
            if (child is XmlElement) {
                dump(child, "$indentation  ", lines)
            } else if (child is XmlContent) {
                lines.add("$indentation  ${child.content}")
            }
        }
        lines.add("$indentation</${element.tag}>")
    }

    @Test
    fun basic() {

        val expected = root("people") {
            element("person", attr("id", "1")) {
                element("name") { content("John Doe") }
                element("age") { content("30") }
            }
            element("person", attr("id", "2")) {
                element("name") { content("Jane Doe") }
                element("age") { content("25") }
            }
        }

        assertEquals(expected, parseXml(basic))
    }

    @Test
    fun svg() {
        assertEquals(svgExpected.dump(), parseXml(svg)?.dump())
    }

    val svgExpected = root(
        "svg",
        attr("id", "thermometer-temperature-svgrepo-com"),
        attr("xmlns", "http://www.w3.org/2000/svg"),
        attr("width", value = "9.744"),
        attr("height", value = "18.343"),
        attr("viewBox", value = "0 0 9.744 18.343")
    ) {
        element(
            "g",
            attr("id", "Group_4"),
            attr("transform", "translate(2.257)")
        ) {
            element(
                "g",
                attr("id", "Group_3")
            ) {
                element(
                    "path",
                    attr("id", "Path_1"),
                    attr("d", "M189.244,11.813V2.5a2.5,2.5,0,1,0-5,0v9.314a3.743,3.743,0,1,0,5,0ZM187.9,8.2h-2.313V2.5a1.156,1.156,0,1,1,2.313,0V8.2Z"),
                    attr("transform", "translate(-183.002)"),
                    attr("fill", "#1ad598")
                )
            }

        }

        element(
            "g",
            attr("id", "Group_6"),
            attr("transform", "translate(0 8.501)")
        ) {
            element(
                "g",
                attr("id", "Group_5")
            ) {
                element(
                    "path",
                    attr("id", "Path_2"),
                    attr("d", "M121.815,237.268h-1.128a.671.671,0,0,0,0,1.342h1.128a.671.671,0,0,0,0-1.342Z"),
                    attr("transform", "translate(-120.016 -237.268)"),
                    attr("fill", "#1ad598")
                )
            }
        }

        element(
            "g",
            attr("id", "Group_8"),
            attr("transform", "translate(0 6.29)")
        ) {
            element(
                "g",
                attr("id", "Group_7")
            ) {
                element(
                    "path",
                    attr("id", "Path_3"),
                    attr("d", "M121.815,175.566h-1.128a.671.671,0,1,0,0,1.342h1.128a.671.671,0,1,0,0-1.342Z"),
                    attr("transform", "translate(-120.016 -175.566)"),
                    attr("fill", "#1ad598")
                )
            }
        }

        element(
            "g",
            attr("id", "Group_10"),
            attr("transform", "translate(0 4.079)")
        ) {
            element(
                "g",
                attr("id", "Group_9")
            ) {
                element(
                    "path",
                    attr("id", "Path_4"),
                    attr("d", "M121.815,113.864h-1.128a.671.671,0,0,0,0,1.342h1.128a.671.671,0,0,0,0-1.342Z"),
                    attr("transform", "translate(-120.016 -113.864)"),
                    attr("fill", "#1ad598")
                )
            }
        }
    }
}