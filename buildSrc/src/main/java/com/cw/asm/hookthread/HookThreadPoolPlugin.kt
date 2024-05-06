package com.cw.asm.hookthread

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class HookThreadPoolPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("hookThread", HookThreadExtension::class.java)
        val android = project.extensions.getByType(AppExtension::class.java)
        android.registerTransform(HookThreadPoolTransform(project))
    }
}
