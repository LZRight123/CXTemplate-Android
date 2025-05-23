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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.aamedium.bounceClick
import com.fantasy.components.aamedium.shakeClickEffect
import com.fantasy.components.extension.compose.addCardBack
import com.fantasy.components.extension.compose.ifTrue
import com.fantasy.components.extension.f1c
import com.fantasy.components.extension.f2c
import com.fantasy.components.extension.f3c
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.mada
import com.fantasy.components.tools.cclog
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CCButton(
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    backgroundColor: Color? = null,
    pressedBackgroundColor: Color? = null,
    pressedIndication: Indication? = null,
    contentColor: Color = CCColor.f1,
    disabledBackgroundColor: Color = (backgroundColor ?: CCColor.b1).copy(alpha = 0.3f),
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
                .ifTrue(isShowShimmer) {
                    Modifier.shimmer()
                }
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
                        cclog("on long click ")
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
                modifier = Modifier//.alpha(if (isLoading) 0.75f else 1f)
            ) {
                content()
            }

            if (isLoading) {
                // 转 shimmer
//                CircularProgressIndicator(
//                    color = contentColor,
//                    strokeWidth = 2.5.dp,
//                    modifier = Modifier.size(18.dp)
//                )
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    PreviewScreen {
        CCButton(
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

        CCButton(
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

        CCButton(
            onClick = { },
            backgroundColor = CCColor.f1,
            contentColor = CCColor.b1,
            isShowShimmer = true,
            modifier = Modifier
                .padding(24.dp)
                .clip(CircleShape)
                .fillMaxWidth()
        ) {
            Text(text = "haha", modifier = Modifier.padding(24.dp))
        }

        CCButton(
            onClick = { },
            isShowShimmer = true,
            backgroundColor = CCColor.b1,
            modifier = Modifier
                .padding(24.dp)
                .clip(CircleShape)
                .fillMaxWidth()
        ) {
            Text(text = "haha", modifier = Modifier.padding(24.dp))
        }


        CCCoilImage(
            model = { null },
            modifier = Modifier.size(180.dp)
        )

        Column {
            val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
            Text("Shimmering Text", modifier = Modifier.shimmer(shimmerInstance))
            Text("Non-shimmering Text")
            Text("Shimmering Text", modifier = Modifier.shimmer(shimmerInstance))

            CCText(text = null, length = 4, style = CCFont.f1.v1.f1c)
            CCText(text = null, length = 4, style = CCFont.f1.v1.f2c)
            CCText(text = null, length = 4, style = CCFont.f1.v1.f3c)
        }

        Row(
            modifier = Modifier
                .shimmer() // <- Affects all subsequent UI elements
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .background(Color.LightGray),
            )


            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(Color.LightGray),
                )
                Box(
                    modifier = Modifier
                        .size(120.dp, 20.dp)
                        .background(Color.LightGray),
                )
            }

        }
    }
}