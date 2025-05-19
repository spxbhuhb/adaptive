package `fun`.adaptive.gradle.js

import com.aayushatharva.brotli4j.Brotli4jLoader
import com.aayushatharva.brotli4j.encoder.BrotliOutputStream
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileOutputStream
import java.util.zip.GZIPOutputStream

abstract class CompressJsResourcesTask : DefaultTask() {

    @InputDirectory
    @Optional
    val inputDir: File = project.file("build/dist/js/productionExecutable")

    private val extensionsToCompress = listOf("js", "css", "html", "json")

    init {
        group = "release"
        description = "Compress JS resources with gzip and brotli for Ktor's preCompressed feature"
    }

    @TaskAction
    fun compress() {
        if (! Brotli4jLoader.isAvailable()) {
            Brotli4jLoader.ensureAvailability() // This loads native lib for current platform
        }

        if (! inputDir.exists()) {
            logger.warn("JS output directory does not exist: $inputDir")
            return
        }

        inputDir.walkTopDown()
            .filter { it.isFile && it.extension in extensionsToCompress }
            .forEach { file ->
                val gzipFile = File(file.absolutePath + ".gz")
                GZIPOutputStream(FileOutputStream(gzipFile)).use { out ->
                    file.inputStream().use { it.copyTo(out) }
                }

                val brotliFile = File(file.absolutePath + ".br")
                BrotliOutputStream(FileOutputStream(brotliFile)).use { out ->
                    file.inputStream().use { it.copyTo(out) }
                }

                logger.lifecycle("Compressed ${file.name} to .gz and .br")
            }
    }
}
