package `fun`.adaptive.sandbox.recipe.writeformat.protobuf

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.encodeToProtoByteArray
import `fun`.adaptive.persistence.append
import `fun`.adaptive.persistence.clearedTestPath
import `fun`.adaptive.persistence.read
import `fun`.adaptive.persistence.resolve
import `fun`.adaptive.persistence.write
import `fun`.adaptive.utility.*
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatDecoder
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlinx.io.files.Path

// This recipe demonstrates how to create an appendable file of protobuf records.
// The file has a header and a number of records.
//
// `create` - creates a file which contains only the header
// `append` - appends a new record to the file without modifying anything already in the file
// `load` - loads the header and all records from the file
//
// This pattern is useful when you actually store the data in memory and the file is just
// persists the data between application restarts.

@Adat
class Header(
    val version: Int,
    val uuid: UUID<Header>
)

@Adat
class Entry(
    val timestamp: Instant,
    val content: String
)

fun appendRecipe() {

    fun create(h: Header, path: Path) {
        path.write(h.encodeToProtoByteArray())
    }

    fun append(entry: Entry, path: Path) {
        path.append(entry.encodeToProtoByteArray())
    }

    fun load(path: Path): Pair<Header, MutableList<Entry>> {
        val bytes = path.read()
        val decoder = ProtoWireFormatDecoder(bytes)

        val header = decoder.records[0].let {
            it.decoder().rawInstance(it, Header)
        }

        val entries = mutableListOf<Entry>()

        for (i in 1 until decoder.records.size) {
            decoder.records[i].let {
                entries += it.decoder().rawInstance(it, Entry)
            }
        }

        return header to entries
    }

    val dir = clearedTestPath()
    val path = dir.resolve("append.pb")

    create(Header(1, UUID()), path)

    append(Entry(now(), "Hello World!"), path)
    append(Entry(now(), "Hello Adaptive!"), path)

    load(path).let { (header, entries) ->
        println(header)
        entries.forEach(::println)
    }

}