package com.fantasy.cctemplate

import android.app.Application
import com.fantasy.components.tools.CCCoil
import com.fantasy.components.tools.CCKV
import com.fantasy.components.tools.isDebugBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (isDebugBuilder) {
                androidLogger()
            }
            // 一定要加，在外部可以通过 getContext 获取 在Preview里才好处理
            androidContext(this@MainApplication)
            modules(module {  })
        }
        CCKV.start() // 同步
        CCCoil.start() // 里面的是异步IO
//        ThirdSDKManager.shared.initSDK() // 有同步初始化 也有异步IO
    }
}