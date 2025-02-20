package com.fantasy.components.widget

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
import com.fantasy.components.extension.f1c
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.navBarHeight
import com.fantasy.components.tools.Apphelper
import com.fantasy.cxtemplate.R
import dev.funkymuse.compose.core.ifFalse
import dev.funkymuse.compose.core.ifNotNull

@Composable
fun CCNavigationBar(
    backgroundColor: Color? = CCColor.b2.copy(0.95f),
    horizontalPadding: Dp = 12.dp,
    ignoreStatusBar: Boolean = false,
    leftView: @Composable (RowScope.() -> Unit)? = null,
    rightView: @Composable (RowScope.() -> Unit)? = null,
    titleView: @Composable (() -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .ifNotNull(backgroundColor) {
                Modifier.addHazeOver(backgroundColor!!)
            }
            .ifFalse(ignoreStatusBar) {
                Modifier.statusBarsPadding()
            }
            .padding(horizontal = horizontalPadding)
            .fillMaxWidth()
            .height(navBarHeight)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leftView?.let {
                it()
            }
        }
        titleView?.let { it() }
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            rightView?.let {
                it()
            }
        }
    }
}

@Composable
fun CCNormalNavigationBar(
    title: String? = null,
    backgroundColor: Color? = CCColor.b2.copy(0.95f),
    ignoreStatusBar: Boolean = false,
    leftView: @Composable (RowScope.() -> Unit)? = null,
    rightView: @Composable (RowScope.() -> Unit)? = null,
    titleView: @Composable (() -> Unit)? = null,
    backAction: (() -> Unit)? = null,
) {
    CCNavigationBar(
        backgroundColor = backgroundColor,
        horizontalPadding = 12.dp,
        ignoreStatusBar = ignoreStatusBar,
        leftView = {
            if (leftView != null) {
                leftView()
            } else {
                Icon(
                    id = R.drawable.system_backbar_btn,
                    modifier = Modifier
                        .fantasyClick(null) {
                            if (backAction != null) {
                                backAction()
                            } else {
                                Apphelper.pop()
                            }
                        }
                        .size(20.dp)
                )
            }
        },
        rightView = rightView,
    ) {
        if (titleView != null) {
            titleView()
        } else {
            title?.let {
                Text(
                    text = it,
                    style = CCFont.f1b.v1.f1c.alignCenter,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.8f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CCNavigationBar(titleView = { Text("Title") })
}