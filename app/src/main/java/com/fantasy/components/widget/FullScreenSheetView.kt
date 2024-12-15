package com.fantasy.components.widget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalInspectionMode
import com.fantasy.components.animations.cxSlideInVertically

@Composable
fun FullScreenSheetView(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    if (LocalInspectionMode.current) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
        return
    }
    if (visible) {
        var isShow by remember {
            mutableStateOf(true)
        }
        CXFullscreenPopup(
            onDismiss = {
                onVisibleChange(false)
            }
        ) {
            BackHandler {
                isShow = false
            }

            Box(
                modifier = Modifier
                    .cxSlideInVertically(
                        show = isShow,
                        duration = 250,
                        onDismissFinished = {
                            onVisibleChange(false)
                        }
                    )
                    .fillMaxSize()
            ) {
                content()
            }

        }
    }
}

@Composable
fun <T> FullScreenSheetView(
    data: T?,
    onDataChange: (T?) -> Unit,
    content: @Composable (T) -> Unit,
) {
    var lastNonNullData by remember { mutableStateOf(data) }
    DisposableEffect(data) {
        if (data != null) lastNonNullData = data
        onDispose {}
    }
    FullScreenSheetView(
        visible = data != null,
        onVisibleChange = { visible ->
            if (visible) {
                onDataChange(lastNonNullData)
            } else {
                onDataChange(null)
            }
        },
    ) {
        lastNonNullData?.let {
            content(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun _preview() {
    PreviewScreen {
        FullScreenSheetView(
            visible = true,
            onVisibleChange = {},
            content = {}
        )
    }
}