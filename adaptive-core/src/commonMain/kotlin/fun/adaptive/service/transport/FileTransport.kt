package `fun`.adaptive.service.transport

import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.utility.UUID
import kotlinx.io.files.Path

abstract class FileTransport {
    abstract fun getDownloadPath(uuid : UUID<ServiceContext>, fileName : String) : Path
}