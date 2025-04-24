package com.fantasy.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fantasy.components.extension.alignCenter
import com.fantasy.components.extension.compose.Icon
import com.fantasy.components.extension.compose.addHazeOver
import com.fantasy.components.extension.compose.fantasyClick
import com.fantasy.components.extension.compose.ifFalse
import com.fantasy.components.extension.compose.ifNotNull
import com.fantasy.components.extension.f1c
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.navBarHeight
import com.fantasy.components.tools.AppHelper
import com.fantasy.cctemplate.R


@Composable
fun CCNavigationBar(
    modifier: Modifier = Modifier,
    horizontalPadding: Int = 24,
    leftView: @Composable (RowScope.() -> Unit)? = null,
    rightView: @Composable (RowScope.() -> Unit)? = null,
    titleView: @Composable (() -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(horizontal = horizontalPadding.dp)
            .fillMaxWidth()
            .statusBarsPadding()
            .height(navBarHeight)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .matchParentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leftView?.let {
                it()
            }
            Spacer(Modifier.weight(1f))
            rightView?.let {
                it()
            }
        }
        titleView?.let { it() }
    }
}

@Composable
fun CCNormalNavigationBar(
    title: String? = null,
    modifier: Modifier = Modifier,
    horizontalPadding: Int = 24,
    leftView: @Composable (RowScope.() -> Unit)? = null,
    rightView: @Composable (RowScope.() -> Unit)? = null,
    titleView: @Composable (() -> Unit)? = null,
) {
    Column {
        CCNavigationBar(
            modifier = modifier,
            leftView =leftView,
            rightView = rightView,
            horizontalPadding = horizontalPadding,
        ) {
            if (titleView != null) {
                titleView()
            } else {
                title?.let {
                    Text(
                        text = it,
                        style = CCFont.f1b.v2.f1c.alignCenter,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.8f),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CCNavigationBar(
        titleView = { Text("Title") },
        modifier = Modifier.background(CCColor.red_t)
    )
}