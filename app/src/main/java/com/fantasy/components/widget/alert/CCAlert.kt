package com.fantasy.components.widget.alert


import android.view.Window
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.sheetBackgroundColor
import com.fantasy.components.tools.screenHeight
import com.fantasy.components.widget.PreviewScreen

@Composable
fun CCAlert(
    visible: Boolean,
    onDismiss: () -> Unit,
    dismissOnClickOutside: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth(),
    content: @Composable ColumnScope.() -> Unit
) {
    if (visible) {
        CCDialog(
            dismissOnClickOutside = dismissOnClickOutside,
            onDismissRequest = onDismiss,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = modifier
                    .background(CCColor.b1, RoundedCornerShape(10.dp))
                    .padding(20.dp)
            ) {
                content()
            }
        }
    }
}

/**
 * 自定义 Dialog 背景色
 * https://www.sinasamaki.com/custom-dialog-animation-in-jetpack-compose/
 */
@Composable
fun CCDialog(
    onDismissRequest: () -> Unit,
    dismissOnClickOutside: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        val dialogWindow = getDialogWindow()

        SideEffect {
            dialogWindow.let { window ->
                window?.setDimAmount(0f)
                window?.setWindowAnimations(-1)
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var animateIn by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { animateIn = true }
            AnimatedVisibility(
                visible = animateIn,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .requiredHeight(screenHeight.dp + 100.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures {
                                if (dismissOnClickOutside) {
                                    onDismissRequest()
                                }
                            }
                        }
                        .background(CCColor.sheetBackgroundColor)
                        .fillMaxSize()
                )
            }
            content()
        }
    }
}

@ReadOnlyComposable
@Composable
fun getDialogWindow(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window

@Preview
@Composable
private fun preview() {
    PreviewScreen { }
}