package com.fantasy.components.tools

// com.fantasy.components.tools.ndLog

class PerformanceManager private constructor() {
    companion object {
        val shared = PerformanceManager()
    }

    // System.currentTimeMillis()
    private var applicationLaunchStart: Long = 0
    private var applicationLaunchEnd: Long = 0

    private var welcomeActivityEnter: Long = 0

    fun applicationLaunchStart() {
        applicationLaunchStart = System.currentTimeMillis()
    }

    fun applicationLaunchEnd() {
        applicationLaunchEnd = System.currentTimeMillis()
        cxlog("性能监测：application 加载时长 ${applicationLaunchEnd - applicationLaunchStart}ms")
    }

    fun welcomeActivityEnter() {
        welcomeActivityEnter = System.currentTimeMillis()
        cxlog("性能监测：从 application 加载到启动 WelcomeActivity 时长 ${welcomeActivityEnter - applicationLaunchStart}ms")
    }
}