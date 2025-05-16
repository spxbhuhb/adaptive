package `fun`.adaptive.value.blob.example

import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.blob.api.AvBlobApi

/**
 * This example shows how to upload a value blob.
 *
 * The client that executes this code must have the permission to upload a blob
 * for the value identified by [valueId].
 */
fun valueBlobUploadExample(valueId: AvValueId, blobService: AvBlobApi) {
    val chunks = listOf(
        byteArrayOf(1, 2, 3),
        byteArrayOf(4, 5, 6),
        byteArrayOf(7, 8, 9)
    )

    val uploadKey = blobService.startUpload(valueId)

    chunks.forEachIndexed { index, chunk ->
        blobService.sendChunk(uploadKey, index * 3L, chunk)
    }

    blobService.finishUpload(uploadKey)
}