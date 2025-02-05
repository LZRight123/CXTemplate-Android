package com.fantasy.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.fantasy.components.animations.cxSlideInVertically
import com.fantasy.components.extension.color
import com.fantasy.components.theme.CXColor
import com.fantasy.components.theme.CXFont
import com.fantasy.components.tools.safeAreaTop
import com.fantasy.components.tools.screenWith
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CXInfoBar(
    message: CXInfoBarMessage?,
    onDismiss: () -> Unit
) {
    if (message != null) {
        CXFullscreenPopup {
            // 为了执行 onDismiss 时动画
            var isShowAnimation by remember {
                mutableStateOf(true)
            }
            var job: Job? = remember {
                null
            }
            LaunchedEffect(message) {
                job?.cancel()
//                ndLog("job cancel")
                job = launch {
                    delay(2_500)
//                    ndLog("job over")

                    if (message.type != CXToastType.loading) {
                        isShowAnimation = false
                    }
                }
            }
            val paddingTop = safeAreaTop + 16.dp
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .cxSlideInVertically(
                        show = isShowAnimation,
                        reverse = true,
                        onDismissFinished = {
                            onDismiss()
                        }
                    )
                    .fillMaxWidth()
                    .padding(top = paddingTop),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            ambientColor = CXColor.black.copy(0.5f),
                            spotColor = CXColor.black.copy(0.5f),
                        )
                        .background(CXColor.white, CircleShape)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .widthIn(min = (screenWith * 0.4).dp, max = (screenWith * 0.618).dp)
                ) {
                    if (message.type == CXToastType.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(13.dp),
                            color = CXColor.f1.copy(0.8f),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                    Text(
                        text = message.text,
                        style = CXFont.f2b.v1.color(message.type.tintColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

        }

    }
}

enum class CXToastType {
    success,
    info,
    warning,
    error,
    loading,
    ;

    val tintColor
        @Composable
        get() = when (this) {
            success -> CXColor.f1
            info -> CXColor.f1
            warning -> CXColor.f2
            error -> CXColor.error
            loading -> CXColor.f1
        }
}

data class CXInfoBarMessage(
    val text: String = "",
    val type: CXToastType = CXToastType.info,
//    val id: String = UUID.randomUUID().toString(),
)


@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen {}
    CXInfoBar(message = CXInfoBarMessage("网络异常，请检查网络设置", CXToastType.error)) {

    }
}