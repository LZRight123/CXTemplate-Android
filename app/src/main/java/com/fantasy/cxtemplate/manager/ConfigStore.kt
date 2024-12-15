package com.fantasy.getgirlsmoney.manager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasy.cxtemplate.model.XMAPPConfig
import kotlinx.coroutines.launch

/**
 * 有的接口在启动app后只需要掉用成功一次，放在这里
 */
class ConfigStore private constructor() : ViewModel() {
    companion object {
        val shared = ConfigStore()
    }

    fun commonFetch() {
        viewModelScope.launch {
            launch { fetchAppConfigIfNeed() }
        }
    }

    var appConfig by mutableStateOf(XMAPPConfig())
        private set

    suspend fun fetchAppConfigIfNeed(forces: Boolean = false): XMAPPConfig {
        if (appConfig.oss_endpoint.isEmpty() || forces) {
//            appConfig = Networking.create<PublicAPI>().config().data ?: XMAPPConfig()
        }
        return appConfig
    }
}

