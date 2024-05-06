package com.cw.asm.hookthread

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ThreadClassVisitor(
    classWriter: ClassWriter,
    private val className: String,
    private val hookList: List<ThreadMethod>
) : ClassVisitor(Opcodes.ASM6, classWriter) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return ThreadMethodVisitor(mv, hookList, className)
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        val matchExtends = hookList.find {
            it.opcode == Opcodes.INVOKESPECIAL && it.name == "<init>" && it.owner == superName
        }
        if (matchExtends != null) {
            println("[ASM] transformExtends match ${className}:${superName} -> ${matchExtends.hook}")
            super.visit(version, access, name, signature, matchExtends.hook, interfaces)
        } else {
            super.visit(version, access, name, signature, superName, interfaces)
        }
    }
}
