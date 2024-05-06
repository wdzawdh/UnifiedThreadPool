package com.cw.asm.hookthread

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.cw.asm.hookthread.utils.FileUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class HookThreadPoolTransform(private val project: Project) : Transform() {

    private val extension by lazy {
        project.extensions.getByType(HookThreadExtension::class.java)
    }

    override fun getName(): String {
        return this.javaClass.simpleName
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_JARS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation) {
        val enableHookThread = extension.enableHookThread
        if (enableHookThread) {
            val inputs = transformInvocation.inputs
            val outputProvider = transformInvocation.outputProvider
            inputs.forEach { input ->
                input.jarInputs.forEach { jarInput ->
                    // 处理Jar
                    processJarInput(jarInput, outputProvider)
                }
                input.directoryInputs.forEach { directoryInput ->
                    // 处理源码文件
                    processDirectoryInputs(directoryInput, outputProvider)
                }
            }
        }
    }

    private fun processJarInput(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        val dest = outputProvider.getContentLocation(
            jarInput.file.absolutePath,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
        // FileUtils.copyFile(jarInput.file, dest)
        val tmpDir = File("${project.buildDir}/tmp/${jarInput.name}")
        tmpDir.mkdirs()
        FileUtils.unzipJar(jarInput.file, tmpDir)
        val classFiles = FileUtils.findAllClassFiles(tmpDir)
        classFiles.forEach { classFile ->
            transform(tmpDir, classFile)
        }
        FileUtils.zipJar(tmpDir, dest)
    }

    private fun processDirectoryInputs(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider
    ) {
        val dest = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        val inputDir = directoryInput.file
        FileUtils.findAllClassFiles(inputDir).forEach { inputFile ->
            transform(inputDir, inputFile)
        }
        // 将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
        if (inputDir.isDirectory) {
            FileUtils.copyDirectory(inputDir, dest)
        } else {
            FileUtils.copyFile(inputDir, dest)
        }
    }

    private fun transform(inputDir: File, inputFile: File) {
        val relativePath = inputFile.relativeTo(inputDir).path
        if (relativePath.endsWith(".class")) {
            val classPath = relativePath.substring(0, relativePath.length - 6)
            val threadMethods = getHookThreadMethods(classPath)
            if (threadMethods.isNotEmpty()) {
                transform(inputFile, threadMethods)
            }
        }
    }

    private fun transform(file: File, hookList: List<ThreadMethod>) {
        val inputStream = FileInputStream(file)
        val classReader = ClassReader(inputStream)
        val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val classVisitor = ThreadClassVisitor(classWriter, file.absolutePath, hookList)
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        val outputStream = FileOutputStream(file)
        outputStream.write(classWriter.toByteArray())
        outputStream.close()
        inputStream.close()
    }

    private fun getHookThreadMethods(classPath: String): List<ThreadMethod> {
        // hook thread pool
        val hookMethods = mutableListOf<ThreadMethod>()
        val excludeHookPaths = extension.excludeHookThreadPaths ?: listOf()
        val hookThread = extension.hookThread
        val hookThreadPool = extension.hookExecutors
        val hookThreadPoolExecutor = extension.hookThreadPoolExecutor
        val hookScheduledThreadPoolExecutor = extension.hookScheduledThreadPoolExecutor
        val limits = listOf(
            hookThread,
            hookThreadPool,
            hookThreadPoolExecutor,
            hookScheduledThreadPoolExecutor
        )
        if (!excludeHookPaths.contains(classPath) && !isLimitClass(classPath, limits)) {
            if (hookThread != null) {
                // java/lang/Thread
                hookMethods.addAll(getThreadMethods(hookThread))
            }
            if (hookThreadPool != null) {
                // java/util/concurrent/Executors
                hookMethods.addAll(getExecutorsMethods(hookThreadPool))
            }
            if (hookThreadPoolExecutor != null) {
                // java/util/concurrent/ThreadPoolExecutor
                hookMethods.addAll(getThreadPoolMethods(hookThreadPoolExecutor))
            }
            if (hookScheduledThreadPoolExecutor != null) {
                // java/util/concurrent/ScheduledThreadPoolExecutor
                hookMethods.addAll(getScheduledThreadPoolMethods(hookScheduledThreadPoolExecutor))
            }
        }
        return hookMethods
    }

    private fun isLimitClass(classPath: String, limits: List<String?>): Boolean {
        return limits.any {
            it != null && classPath.startsWith(it)
        }
    }
}
