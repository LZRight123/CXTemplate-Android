package com.fantasy.components.widget

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fantasy.components.tools.localDelay
import kotlinx.coroutines.*

@Composable
fun CXToast(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
    toastDuration: Long = 3000,
    animationDuration: Int = 350,
    enter: EnterTransition = slideInVertically(
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutLinearInEasing,
        ),
        initialOffsetY = { -it }
    ) + fadeIn(),
    exit: ExitTransition = slideOutVertically(
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutLinearInEasing,
        ),
        targetOffsetY = { -it }
    ) + fadeOut(),
    content: @Composable ColumnScope.() -> Unit,
) {
    var popupVisible by remember {
        mutableStateOf(false)
    }
    if (!visible && !popupVisible) {
        return
    }

    LaunchedEffect(visible) {
        popupVisible = true
        localDelay(toastDuration)
        popupVisible = false
        localDelay(animationDuration.toLong())
        onVisibleChange(false)
    }

    CXFullscreenPopup(onDismiss = {
        onVisibleChange(false)
    }) {
        AnimatedVisibility(
            visible = popupVisible,
            enter = enter,
            exit = exit,
            modifier = Modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                content()
            }
        }
    }
}


@Composable
fun <T> CXToast(
    data: T?,
    onDataChange: (T?) -> Unit,
    toastDuration: Long = 3000,
    animationDuration: Int = 350,
    enter: EnterTransition = slideInVertically(
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutLinearInEasing,
        ),
        initialOffsetY = { -it }
    ) + fadeIn(),
    exit: ExitTransition = slideOutVertically(
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutLinearInEasing,
        ),
        targetOffsetY = { -it }
    ) + fadeOut(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (data != null) {
        var lastNonNullData by remember { mutableStateOf<T?>(null) }
        var job: Job? by remember { mutableStateOf(null) }
        LaunchedEffect(data) {
            job?.cancel()
            job = launch {
                lastNonNullData = data
                localDelay(toastDuration)
                lastNonNullData = null
                localDelay(animationDuration.toLong())
                onDataChange(null)
            }

        }

        CXFullscreenPopup(onDismiss = {
            onDataChange(null)
        }) {
            AnimatedVisibility(
                visible = lastNonNullData != null,
                enter = enter,
                exit = exit,
                modifier = Modifier
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    content()
                }
            }
        }
    }
}


//@Stable
//class FantasyToastDataState<T>(data: T?) {
//    var data: T? by mutableStateOf(data)
//        private set
//    fun toast(data: T) {
//        this.data = data
//    }
//    fun dismiss() {
//        data = null
//    }
//}
//
//@Composable
//fun <T> rememberFantasyToastState(
//    data: T? = null
//): FantasyToastDataState<T> = remember {
//    FantasyToastDataState(data)
//}

@Preview
@Composable
private fun Preview() {
    CXScaffold {
        Text("!23")
        CXToast(data = "Hello World", onDataChange = {}) {

        }
    }
}