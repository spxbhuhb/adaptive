package `fun`.adaptive.value.blob.api

import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.blob.AvBlobUploadKey

@ServiceApi
interface AvBlobApi {

    /**
     * Start a [value blob](def://) upload.
     *
     * A [session](def://) is required to start an upload.
     *
     * Adds the [blob:uploading](marker://) marker to the [value](def://).
     *
     * @throws  IllegalStateException  If there is no session in the service context.
     */
    suspend fun startUpload(valueId : AvValueId, size : Long) : AvBlobUploadKey

    /**
     * Send a chunk of a [value blob](def://). A blob upload process must be started
     * by calling [startUpload] before sending chunks.
     *
     * Chunks do not have to be sent in order.
     */
    suspend fun sendChunk(uploadId: AvBlobUploadKey, offset: Long, data: ByteArray)

    /**
     * Finish a [value blob](def://) upload.
     *
     * Once finished, the blob will be available for reading.
     *
     * Removes the [blob:uploading](marker://) marker from the [value](def://).
     * Adds the [blob](marker://) marker to the [value](def://).
     */
    suspend fun finishUpload(uploadId: AvBlobUploadKey)

    /**
     * Abort a [value blob](def://) upload.
     *
     * Deletes all uploaded data and all associated metadata.
     *
     * Removes the [blob:uploading](marker://) marker from the [value](def://).
     */
    suspend fun abortUpload(uploadId: AvBlobUploadKey)

}