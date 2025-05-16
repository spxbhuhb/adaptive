package `fun`.adaptive.value.blob.server

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.blob.AvBlobUploadKey
import `fun`.adaptive.value.blob.api.AvBlobApi

class AvBlobService : ServiceImpl<AvBlobService>(), AvBlobApi {

    val blobWorker by workerImpl<AvBlobWorker>()

    override suspend fun startUpload(valueId: AvValueId, size: Long): AvBlobUploadKey =
        blobWorker.startUpload(serviceContext.session, valueId, size)

    override suspend fun sendChunk(uploadId: AvBlobUploadKey, offset: Long, data: ByteArray) {
        blobWorker.addChunk(serviceContext.session, uploadId, offset, data)
    }

    override suspend fun finishUpload(uploadId: AvBlobUploadKey) {
        blobWorker.finishUpload(serviceContext.session, uploadId)
    }

    override suspend fun abortUpload(uploadId: AvBlobUploadKey) {
        blobWorker.abortUpload(serviceContext.session, uploadId)
    }

}