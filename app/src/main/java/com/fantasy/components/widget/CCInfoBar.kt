package com.fantasy.components.widget

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.aamedium.outerShadow
import com.fantasy.components.animations.ccSlideInVertically
import com.fantasy.components.extension.color
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.safeAreaTop
import com.fantasy.components.tools.screenWidth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

@Composable
fun CCInfoBar(
    message: CCInfoBarMessage?,
    duration: Long = 3000,
    onDismiss: (CCInfoBarMessage) -> Unit
) {
    if (message != null) {
        CCFullscreenPopup {
            val paddingTop = safeAreaTop + 16.dp
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = paddingTop),
            ) {
                CCInfoBarContent(message = message, duration = duration) {
                    onDismiss(message)
                }
            }

        }

    }
}

@Composable
private fun CCInfoBarContent(
    message: CCInfoBarMessage,
    duration: Long = 3000,
    onDismiss: () -> Unit = {}
) {
    // 为了执行 onDismiss 时动画
    var isShowAnimation by remember {
        mutableStateOf(true)
    }
    var job: Job? = remember {
        null
    }
    LaunchedEffect(message) {
        job?.cancel()
        job = launch {
            delay(duration)

            if (message.type != CCToastType.loading) {
                isShowAnimation = false
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .ccSlideInVertically(
                show = isShowAnimation,
                reverse = true,
                onDismissFinished = {
                    onDismiss()
                }
            )
            .outerShadow(
                shape = CircleShape,
                color = CCColor.f1.copy(0.1f),
                radius = 8
            )
            .background(CCColor.white, CircleShape)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .widthIn(min = (screenWidth * 0.4).dp, max = (screenWidth * 0.618).dp)
    ) {
        if (message.type == CCToastType.loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(13.dp),
                color = CCColor.f1.copy(0.8f),
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = message.text,
            style = CCFont.f2b.v1.color(message.type.tintColor),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

enum class CCToastType {
    success,
    info,
    warning,
    error,
    loading,
    ;

    val tintColor
        @Composable
        get() = when (this) {
            success -> CCColor.f1
            info -> CCColor.f1
            warning -> CCColor.f2
            error -> CCColor.error
            loading -> CCColor.f1
        }
}

data class CCInfoBarMessage(
    val text: String = "",
    val type: CCToastType = CCToastType.info,
//    val id: String = UUID.randomUUID().toString(),
)


@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen(
        verticalArrangement = 20,
        modifier = Modifier.padding(top = 100.dp)
    ) {
        CCToastType.entries.forEach {
            CCInfoBarContent( CCInfoBarMessage("网络异常，请检查网络设置", it))
        }
    }

    CCInfoBar(message = CCInfoBarMessage("网络异常，请检查网络设置", CCToastType.error)) {

    }

}