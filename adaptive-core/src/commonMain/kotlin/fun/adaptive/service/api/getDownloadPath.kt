package `fun`.adaptive.service.api

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.transport.FileTransport

/**
 * Get a path for a file download. Use the returned path to create a file which can be downloaded
 * within the same session by accessing `/adaptive/download/<fileName>`.
 *
 * **IMPORTANT** "within the same session"
 *
 * * The path is created by the [FileTransport] in [ServiceContext].
 * * The actual download path might be different if settings are not on default.
 *
 * With `adaptive-lib-ktor`:
 *
 * * `KTOR_DOWNLOAD_DIR` contains the directory of all downloads, default: `./var/download`
 * * `KTOR_DOWNLOAD_ROUTE` contains the URL for all downloads, default: `/adaptive/download`
 * * the file is deleted after the download (be it success or error)
 * * the actual path is: `./var/download/<session-id>/<fileName>`
 *
 * @param fileName Name of the file to download, must match `[\w\-. ]+`.
 *
 * @throws  IllegalStateException  if there is no [FileTransport] in the context or if there is no session in the context
 */
fun ServiceImpl<*>.getDownloadPath(fileName: String) =
    serviceContext.getDownloadPath(fileName)

/**
 * Get a path for a file download. Use the returned path to create a file which can be downloaded
 * within the same session by accessing `/adaptive/download/<fileName>`.
 *
 * **IMPORTANT** "within the same session"
 *
 * * The path is created by the [FileTransport] in [ServiceContext].
 * * The actual download path might be different if settings are not on default.
 *
 * With `adaptive-lib-ktor`:
 *
 * * `KTOR_DOWNLOAD_DIR` contains the directory of all downloads, default: `./var/download`
 * * `KTOR_DOWNLOAD_ROUTE` contains the URL for all downloads, default: `/adaptive/download`
 * * the file is deleted after the download (be it success or error)
 * * the actual path is: `./var/download/<session-id>/<fileName>`
 *
 * @param fileName Name of the file to download, must match `[\w\-. ]+`.
 *
 * @throws  IllegalStateException  if there is no [FileTransport] in the context or if there is no session in the context
 */
fun ServiceContext.getDownloadPath(fileName: String) =
    checkNotNull(fileTransport) { "no file transport in the service context" }.getDownloadPath(uuid, fileName)