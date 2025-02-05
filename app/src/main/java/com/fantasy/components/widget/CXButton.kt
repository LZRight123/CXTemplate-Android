package com.fantasy.components.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.aamedium.bounceClick
import com.fantasy.components.aamedium.shakeClickEffect
import com.fantasy.components.extension.compose.addCardBack
import com.fantasy.components.extension.cxshimmer
import com.fantasy.components.theme.CXColor
import com.fantasy.components.tools.mada
import com.fantasy.components.tools.cxlog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CXButton(
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    backgroundColor: Color? = null,
    pressedBackgroundColor: Color? = null,
    pressedIndication: Indication? = null,
    contentColor: Color = CXColor.f1,
    disabledBackgroundColor: Color = (backgroundColor ?: CXColor.b1).copy(alpha = 0.3f),
    disabledContentColor: Color = contentColor.copy(alpha = 0.3f),
    pressedScale: Float = 0.98f,
    isShowShimmer: Boolean = false,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scope = rememberCoroutineScope()
    var clickWithOutPressed by remember {
        mutableStateOf(false)
    }
    var canClick by remember {
        mutableStateOf(enabled && !isLoading)
    }
    LaunchedEffect(enabled, isLoading) {
        canClick = enabled && !isLoading
    }

    CompositionLocalProvider(
        LocalContentColor provides if (enabled) contentColor else disabledContentColor,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .then(
                    if (isShowShimmer) Modifier.cxshimmer(600) else Modifier
                )
                .bounceClick(canClick, pressedScale)
                .shakeClickEffect(!canClick)
                .combinedClickable(
                    enabled = true,
                    interactionSource = interactionSource,
                    indication = pressedIndication,
                    onClick = {
                        mada()
                        scope.launch {
                            clickWithOutPressed = true
                            delay(400)
                            clickWithOutPressed = false
                        }
                        if (canClick) {
                            onClick()
                        }
                    },
                    onLongClick = if (onLongClick == null) null else ({
                        cxlog("on long click ")
                        mada()
                        scope.launch {
                            clickWithOutPressed = true
                            delay(400)
                            clickWithOutPressed = false
                        }
                        if (canClick) {
                            onLongClick()
                        }
                    }),
                )
                .then(modifier)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        if (enabled) (backgroundColor ?: Color.Transparent)
                        else disabledBackgroundColor
                    )
                    .matchParentSize()
            )

            // 点击时改颜色
            if (enabled && pressedBackgroundColor != null) {
                AnimatedVisibility(
                    visible = isPressed || clickWithOutPressed,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.matchParentSize()
                ) {
                    Box(
                        modifier = Modifier
                            .background(pressedBackgroundColor)
                            .matchParentSize()
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.alpha(if (isLoading) 0.75f else 1f)
            ) {
                content()
            }

            if (isLoading) {
                CircularProgressIndicator(
                    color = contentColor,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}



@Preview
@Composable
private fun Preview() {
    PreviewScreen {
        CXButton(
            onClick = { },
            modifier = Modifier.addCardBack()
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "haha")
            }
        }

        CXButton(
            onClick = { },
            modifier = Modifier.addCardBack()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp, 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "haha")
            }
        }


    }
}