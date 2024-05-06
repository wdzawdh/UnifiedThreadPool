package com.cw.asm.hookthread


data class ThreadMethod(
    val opcode: Int,
    val owner: String,
    val name: String,
    val descriptor: String,
    val isInterface: Boolean = false,
    val hook: String
) {
    fun equalMethod(
        opcode: Int, owner: String?, name: String?, descriptor: String?
    ): Boolean {
        return this.opcode == opcode && this.owner == owner && this.name == name && this.descriptor == descriptor
    }

    fun equalNew(
        type: String?
    ): Boolean {
        return this.owner == type
    }
}
