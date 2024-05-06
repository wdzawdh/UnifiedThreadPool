package com.cw.asm.hookthread.utils

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.ArrayList

object FileUtils {

    fun listClasses(directory: File): List<File> {
        val files = ArrayList<File>()
        listClasses(directory, files)
        return files
    }

    fun listClasses(directory: File, list: ArrayList<File>) {
        if (directory.isDirectory) {
            directory.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    listClasses(file, list)
                } else {
                    if (file.name.endsWith(".class")) {
                        list.add(file)
                    }
                }
            }
        } else {
            if (directory.name.endsWith(".class")) {
                list.add(directory)
            }
        }
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
}

