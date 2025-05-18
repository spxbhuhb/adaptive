package `fun`.adaptive.value.blob.server

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.blob.AvBlobUploadKey
import `fun`.adaptive.value.blob.api.AvBlobApi

class AvBlobService : ServiceImpl<AvBlobService>(), AvBlobApi {

    val blobWorker by workerImpl<AvBlobWorker>()

    override suspend fun startUpload(valueId: AvValueId, size: Long): AvBlobUploadKey =
        blobWorker.startUpload(serviceContext.session, valueId, size)

    override suspend fun sendChunk(uploadKey: AvBlobUploadKey, offset: Long, data: ByteArray) {
        blobWorker.addChunk(serviceContext.session, uploadKey, offset, data)
    }

    override suspend fun finishUpload(uploadKey: AvBlobUploadKey, sha256: ByteArray?) {
        blobWorker.finishUpload(serviceContext.session, uploadKey, sha256)
    }

    override suspend fun abortUpload(uploadKey: AvBlobUploadKey) {
        blobWorker.abortUpload(serviceContext.session, uploadKey)
    }

    override suspend fun removeBlob(valueId: AvValueId) {
        blobWorker.removeBlob(serviceContext.session, valueId)
    }

}