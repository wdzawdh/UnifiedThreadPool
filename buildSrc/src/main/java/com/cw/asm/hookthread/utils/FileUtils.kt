package com.cw.asm.hookthread.utils

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.ArrayList
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

object FileUtils {

    fun findAllClassFiles(dir: File): List<File> {
        val classFiles = mutableListOf<File>()
        dir.walkTopDown().forEach {
            if (it.isFile && it.name.endsWith(".class")) {
                classFiles.add(it)
            }
        }
        return classFiles
    }

    @Throws(Exception::class)
    fun copyDirectory(sourceDirectory: File, destinationDirectory: File) {
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs()
        }
        val files = sourceDirectory.listFiles()
        files?.forEach { file ->
            val destination = File(destinationDirectory, file.name)
            if (file.isDirectory) {
                copyDirectory(file, destination)
            } else {
                copyFile(file, destination)
            }
        }
    }

    @Throws(Exception::class)
    fun copyFile(sourceFile: File, destinationFile: File) {
        Files.copy(
            sourceFile.toPath(),
            destinationFile.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }

    fun unzipJar(inputJar: File, outputDir: File) {
        val zipFile = ZipFile(inputJar)
        zipFile.use { zip ->
            zip.entries().asSequence().forEach { entry ->
                val entryFile = File(outputDir, entry.name)
                if (entry.isDirectory) {
                    entryFile.mkdirs()
                } else {
                    entryFile.parentFile.mkdirs()
                    zip.getInputStream(entry).use { input ->
                        entryFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    }

    fun zipJar(inputDir: File, outputJar: File) {
        ZipOutputStream(FileOutputStream(outputJar)).use { zip ->
            inputDir.walkTopDown().forEach { file ->
                if (file.isFile) {
                    val relativePath = inputDir.toPath().relativize(file.toPath()).toString()
                    zip.putNextEntry(ZipEntry(relativePath))
                    file.inputStream().use { input ->
                        input.copyTo(zip)
                    }
                    zip.closeEntry()
                }
            }
        }
    }
}

