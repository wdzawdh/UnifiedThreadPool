package com.cw.asm.hookthread

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ThreadMethodVisitor(
    methodVisitor: MethodVisitor?,
    private val hookList: List<ThreadMethod>,
    private val className: String,
) : MethodVisitor(Opcodes.ASM6, methodVisitor) {

    override fun visitTypeInsn(opcode: Int, type: String?) {
        val matchNew = matchNew(type)
        if (opcode == Opcodes.NEW && matchNew != null) {
            println("[ASM] transformNew match ${className}:${type} -> ${matchNew.hook}")
            super.visitTypeInsn(opcode, matchNew.hook)
        } else {
            super.visitTypeInsn(opcode, type)
        }
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        val matchOpcode = opcode == Opcodes.INVOKESPECIAL || opcode == Opcodes.INVOKESTATIC
        val matchHook = matchMethod(opcode, owner, name, descriptor)
        if (matchOpcode && matchHook != null) {
            println("[ASM] transformMethod match ${className}:${owner}:${name} -> ${matchHook.hook}:${matchHook.name}")
            //替换
            super.visitMethodInsn(
                opcode,
                matchHook.hook,
                name,
                descriptor,
                isInterface
            )
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }
    }

    private fun matchNew(
        type: String?
    ): ThreadMethod? {
        hookList.forEach {
            if (it.equalNew(type)) {
                return it
            }
        }
        return null
    }

    private fun matchMethod(
        opcode: Int, owner: String?, name: String?, descriptor: String?
    ): ThreadMethod? {
        hookList.forEach {
            if (it.equalMethod(opcode, owner, name, descriptor)) {
                return it
            }
        }
        return null
    }
}
