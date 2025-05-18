package `fun`.adaptive.value.blob.server

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.lib.util.path.UuidFileStore
import `fun`.adaptive.persistence.delete
import `fun`.adaptive.persistence.exists
import `fun`.adaptive.persistence.getRandomAccess
import `fun`.adaptive.persistence.metadata
import `fun`.adaptive.persistence.move
import `fun`.adaptive.persistence.resolve
import `fun`.adaptive.resource.ResourceMetadata
import `fun`.adaptive.service.ServiceSessionId
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.utility.*
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.blob.AvBlobUploadKey
import `fun`.adaptive.value.model.AvValueMarkers
import kotlinx.io.files.Path

class AvBlobWorker(
    val root: Path,
    val levels: Int = 2,
) : WorkerImpl<AvBlobWorker>() {

    val valueWorker by workerImpl<AvValueWorker>()

    val lock = getLock()

    private val uploads = mutableMapOf<AvBlobUploadKey, AvBlobUpload<Path>>()

    private val metadata = mutableMapOf<AvValueId, ResourceMetadata>()

    private val temporaryDir = root.resolve("upload.temporary")

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

        val uploadKey = uuid4<AvBlobUpload<*>>()
        val uploadKeyAsString = uploadKey.toString()

        val dataPath = temporaryDir.resolve("${uploadKeyAsString}.upload")
        val statusPath = temporaryDir.resolve("${uploadKeyAsString}.status")

        lock.use {

            uploads[uploadKey] = AvBlobUpload(
                session.uuid,
                valueId,
                uploadKey,
                size,
                dataPath,
                dataPath.getRandomAccess("rw"),
                statusPath.getRandomAccess("rw"),
            )
        }

        logger.info { "value blob upload start, valueId=${valueId} tmpPath=${dataPath} statusPath=$statusPath size=$size" }

        return uploadKey
    }

    suspend fun addChunk(session: ServiceSession, uploadKey: AvBlobUploadKey, offset: Long, data: ByteArray) {
        val upload = findUpload(session.uuid, uploadKey)
        try {
            upload.add(data, offset)
        } catch (ex : Exception) {
            failCleanup(upload, ex)
            throw ex
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun finishUpload(sessionId: ServiceSession, uploadKey: AvBlobUploadKey, sha256: ByteArray?) {
        val upload = findUpload(sessionId.uuid, uploadKey)

        val storePath = store.pathFor(upload.valueId).resolve("${upload.valueId}.blob")
        val blobMetadata : ResourceMetadata

        try {
            upload.close(sha256) // deletes the status file
            upload.dataPath.move(storePath)

            blobMetadata = storePath.metadata

            lock.use {
                metadata[upload.valueId] = blobMetadata
                uploads.remove(uploadKey)
            }

            valueWorker.replaceMarker(upload.valueId, AvValueMarkers.BLOB_UPLOADING, AvValueMarkers.BLOB, exclusive = true)

        } catch (ex: Exception) {
            failCleanup(upload, ex, storePath)
            throw ex
        }

        logger.info { "value blob upload finish, valueId=${upload.valueId} size=${blobMetadata.size} sha256=${sha256?.toHexString()}" }
    }

    suspend fun abortUpload(sessionId: ServiceSession, uploadKey: AvBlobUploadKey) {
        val upload = findUpload(sessionId.uuid, uploadKey)
        logger.info { "value blob upload abort, valueId=${upload.valueId}" }
        failCleanup(upload)
    }

    private fun findUpload(sessionId: ServiceSessionId, uploadId: AvBlobUploadKey): AvBlobUpload<Path> =
        lock.use {
            val upload = uploads[uploadId] ?: throw IllegalArgumentException("Upload $uploadId not found")
            if (upload.sessionId != sessionId) throw IllegalArgumentException("Upload $uploadId not owned by session $sessionId")
            upload
        }

    private suspend fun failCleanup(upload: AvBlobUpload<Path>, ex : Exception? = null, storePath: Path? = null) {
        if (ex != null) {
            logger.warning(ex) { "value blob upload fail, valueId=${upload.valueId} uploadKey=${upload.key} tmpPath=${upload.dataPath} toPath=$storePath" }
        }

        upload.abort(throwException = false)

        lock.use {
            uploads.remove(upload.key)
        }

        safeSuspendCall(logger) {
            valueWorker.removeMarker(upload.valueId, AvValueMarkers.BLOB_UPLOADING)
        }
    }

    suspend fun removeBlob(session: ServiceSession, valueId: AvValueId) {
        valueWorker.ensureBlobRemoveAccess(session, valueId)

        safeCall(logger) {
            val path = store.pathFor(valueId).resolve("${valueId}.blob")

            if (path.exists()) {
                path.delete(mustExists = false)
            }

            lock.use {
                metadata.remove(valueId)
            }
        }

        valueWorker.removeMarker(valueId, AvValueMarkers.BLOB)
    }

}