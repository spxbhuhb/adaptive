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
     *
     * @throws  IllegalArgumentException  If [uploadKey] is invalid.
     */
    suspend fun sendChunk(uploadKey: AvBlobUploadKey, offset: Long, data: ByteArray)

    /**
     * Finish a [value blob](def://) upload.
     *
     * Once finished, the blob will be available for reading.
     *
     * Removes the [blob:uploading](marker://) marker from the [value](def://).
     * Adds the [blob](marker://) marker to the [value](def://).
     *
     * @throws  IllegalArgumentException  If [uploadKey] is invalid.
     * @throws  AvUploadAbortException    If [sha256] is provided, and it is not equal to the calculated SHA-256 of the uploaded data.
     * @throws  AvUploadAbortException    If data is missing from the upload.
     * @throws  AvUploadAbortException    If moving the uploaded blob from the temporary location to the permanent location fails.
     */
    suspend fun finishUpload(uploadKey: AvBlobUploadKey, sha256: ByteArray? = null)

    /**
     * Abort a [value blob](def://) upload.
     *
     * Deletes all uploaded data and all associated metadata.
     *
     * Removes the [blob:uploading](marker://) marker from the [value](def://).
     */
    suspend fun abortUpload(uploadKey: AvBlobUploadKey)

    /**
     * Remove a [value blob](def://) from the value identified by [valueId].
     *
     * 1. Removes the [blob](marker://) marker from the [value](def://).
     * 2. Removes all uploaded data and all associated metadata.
     */
    suspend fun removeBlob(valueId: AvValueId)
}