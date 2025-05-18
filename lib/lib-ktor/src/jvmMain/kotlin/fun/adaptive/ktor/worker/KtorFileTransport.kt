package `fun`.adaptive.ktor.worker

import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.transport.FileTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.persistence.ensure
import kotlinx.io.files.Path

class KtorFileTransport(
    val worker: KtorWorker,
) : FileTransport() {

    override fun getDownloadPath(uuid: UUID<ServiceContext>, fileName: String): Path =
        Path(Path(worker.downloadPath, uuid.toString()).ensure(), fileName)

}