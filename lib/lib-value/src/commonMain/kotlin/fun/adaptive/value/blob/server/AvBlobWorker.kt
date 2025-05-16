package `fun`.adaptive.value.blob.server

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.file.metadata
import `fun`.adaptive.file.resolve
import `fun`.adaptive.lib.util.path.UuidFileStore
import `fun`.adaptive.resource.ResourceMetadata
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.blob.AvBlobUploadKey
import `fun`.adaptive.value.model.AvValueMarkers
import kotlinx.io.files.Path
import kotlin.collections.set

class AvBlobWorker(
    val root : Path,
    val levels : Int = 2,
) : WorkerImpl<AvBlobWorker>() {

    val valueWorker by workerImpl<AvValueWorker>()

    val lock = getLock()

    private val uploads = mutableMapOf<AvBlobUploadKey, AvBlobUpload>()

    private val metadata = mutableMapOf<AvValueId, ResourceMetadata>()

    private val store = object : UuidFileStore<MutableMap<AvValueId, ResourceMetadata>>(root, levels) {

        override fun loadPath(path: Path, context: MutableMap<AvValueId, ResourceMetadata>) {
            if (! path.name.endsWith(".blob")) return

            try {
                val uuid = UUID<AvValue<*>>(path.name.substringBefore(".blob"))

                context[uuid] = path.metadata

            } catch (ex: Exception) {
                throw RuntimeException("error while loading value from $path", ex)
            }
        }

    }

    suspend fun startUpload(session: ServiceSession, valueId: AvValueId, size: Long): AvBlobUploadKey {
        valueWorker.ensureBlobCreateAccess(session, valueId)

        valueWorker.addMarker(valueId, AvValueMarkers.BLOB_UPLOADING, exclusive = true)

        val uploadKey = uuid4<AvBlobUpload>()
        val uploadKeyAsString = uploadKey.toString()

        lock.use {
            val uploadPath = store.pathFor(valueId)

            uploads[uploadKey] = AvBlobUpload(
                session.uuid,
                valueId,
                uploadKey,
                size,
                uploadPath.resolve("${uploadKeyAsString}.upload"),
                uploadPath.resolve("${uploadKeyAsString}.status"),
            )
        }

        return uploadKey
    }

    fun addChunk(sessionId: ServiceSession, uploadId: AvBlobUploadKey, offset: Long, data: ByteArray) {
        val upload = lock.use {
            val upload = uploads[uploadId] ?: throw IllegalArgumentException("Upload $uploadId not found")
            if (upload.sessionId != sessionId.uuid) throw IllegalArgumentException("Upload $uploadId not owned by session $sessionId")
            upload
        }

        upload.add(data, offset)
    }

    fun finishUpload(sessionId: ServiceSession, uploadId: AvBlobUploadKey) {

    }

    fun abortUpload(sessionId: ServiceSession, uploadId: AvBlobUploadKey) {

    }


}