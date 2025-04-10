@file:Suppress("OPT_IN_USAGE")

package `fun`.adaptive.lib.util.zip

import `fun`.adaptive.lib.util.bytearray.calculateCrc32
import `fun`.adaptive.lib.util.bytearray.toKotlinArray
import `fun`.adaptive.lib.util.bytearray.toPlatformArray
import kotlinx.io.*
import org.khronos.webgl.Uint8Array

class ZipBuilder {

    private val output = Buffer()
    private val entries = mutableListOf<ZipEntry>()

    suspend fun add(filename: String, data: ByteArray) {
        val deflated = deflate(data.toPlatformArray())
        val compressed = Uint8Array(deflated).toKotlinArray()
        val crc32 = calculateCrc32(data)
        val localHeaderOffset = output.size
        val filenameBytes = filename.encodeToByteArray()

        // === Local File Header ===
        with (output) {
            writeIntLe(0x04034b50) // Local file header signature
            writeShortLe(20)       // Version needed to extract
            writeShortLe(0)        // General purpose bit flag
            writeShortLe(8)        // Compression method: DEFLATE (8)
            writeShortLe(0)        // Last mod time
            writeShortLe(0)        // Last mod date
            writeUIntLe(crc32)           // CRC-32
            writeIntLe(compressed.size)  // Compressed size
            writeIntLe(data.size)        // Uncompressed size
            writeShortLe(filenameBytes.size.toShort()) // File name length
            writeShortLe(0)              // Extra field length
            write(filenameBytes)         // File name

            // === Compressed Data ===
            write(compressed)
        }
        
        entries.add(
            ZipEntry(
                filenameBytes = filenameBytes,
                crc32 = crc32,
                compressedSize = compressed.size,
                uncompressedSize = data.size,
                localHeaderOffset = localHeaderOffset.toInt()
            )
        )
    }

    fun finalize(): ByteArray {
        val centralDirectoryOffset = output.size

        // === Central Directory ===
        for (entry in entries) {
            with(output) {
                writeIntLe(0x02014b50)        // Central file header signature
                writeShortLe(0x14)            // Version made by
                writeShortLe(20)              // Version needed to extract
                writeShortLe(0)               // General purpose bit flag
                writeShortLe(8)               // Compression method
                writeShortLe(0)               // Last mod time
                writeShortLe(0)               // Last mod date
                writeUIntLe(entry.crc32)      // CRC-32
                writeIntLe(entry.compressedSize)      // Compressed size
                writeIntLe(entry.uncompressedSize)    // Uncompressed size
                writeShortLe(entry.filenameBytes.size.toShort())  // File name length
                writeShortLe(0)                 // Extra field length
                writeShortLe(0)                 // File comment length
                writeShortLe(0)                 // Disk number start
                writeShortLe(0)                 // Internal file attributes
                writeIntLe(0)                   // External file attributes
                writeIntLe(entry.localHeaderOffset)
                write(entry.filenameBytes)
            }
        }

        // === End of Central Directory ===
        val centralDirectorySize = output.size - centralDirectoryOffset
        with(output) {
            writeIntLe(0x06054b50) // End of central dir signature
            writeShortLe(0)        // Number of this disk
            writeShortLe(0)        // Disk with central dir start
            writeShortLe(entries.size.toShort())  // Number of entries on this disk
            writeShortLe(entries.size.toShort())  // Total number of entries
            writeIntLe(centralDirectorySize.toInt())     // Size of central dir
            writeIntLe(centralDirectoryOffset.toInt())   // Offset of central dir
            writeShortLe(0)       // ZIP file comment length
        }

        return output.readByteArray()
    }

    private class ZipEntry(
        val filenameBytes: ByteArray,
        val crc32: UInt,
        val compressedSize: Int,
        val uncompressedSize: Int,
        val localHeaderOffset: Int
    )
}



