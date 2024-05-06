package com.cw.asm.hookthread

open class HookThreadExtension {
    var enableHookThread: Boolean = false
    var hookThread: String? = null
    var hookExecutors: String? = null
    var hookThreadPoolExecutor: String? = null
    var hookScheduledThreadPoolExecutor: String? = null
    var excludeHookThreadPaths: List<String>? = null
}
