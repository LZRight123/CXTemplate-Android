package com.fantasy.components.widget.alert


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.fantasy.components.theme.CCFont
import com.fantasy.components.theme.CCColor
import com.fantasy.components.widget.PreviewScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fantasy.components.extension.alignCenter
import com.fantasy.components.extension.f1c
import com.fantasy.components.extension.f2c
import com.fantasy.components.extension.randomString
import com.fantasy.components.widget.CCButton

class CCNormalAlertViewModel() : ViewModel() {
    var visible by mutableStateOf(false)
        private set
    var title by mutableStateOf("")
        private set
    var content by mutableStateOf<String?>("")
        private set
    var subContent by mutableStateOf<String?>("")
        private set
    var leftTitle by mutableStateOf<String?>("")
        private set
    var rightTitle by mutableStateOf("")
        private set
    private var onLeft: (() -> Unit) = { }

    private var onRight: (() -> Unit) = { }
    var toolbar: @Composable (() -> Unit)? = null
        private set

    fun show(
        title: String = "温馨提示",
        content: String? = null,
//        subContent: String? = null,
        leftTitle: String? = null, // isNullOrEmpty 没有这个按钮
        rightTitle: String = "确定",
        onLeft: () -> Unit = { dismiss() },
        onRight: () -> Unit = { dismiss() },
    ) {
        this.title = title
        this.content = content
        this.subContent = null
        this.leftTitle = leftTitle
        this.rightTitle = rightTitle
        this.onLeft = onLeft
        this.onRight = onRight
        this.toolbar = null
        visible = true
        show(
            title = title,
            content = content,
            leftView = {
                if (!leftTitle.isNullOrEmpty()) {
                    alertStockButton(
                        text = leftTitle,
                    ) {
                        onLeft()
                        dismiss()
                    }
                }
            },
            rightView = {
                alertFullButton(
                    text = rightTitle,
                ) {
                    onRight()
                    dismiss()
                }
            }
        )
    }


    fun show(
        title: String = "温馨提示",
        content: String? = null,
        leftView: @Composable (() -> Unit),
        rightView: @Composable () -> Unit,
    ) {
        show(
            title = title,
            content = content,
            toolbar = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    leftView()
                    rightView()
                }
            }
        )
    }

    fun show(
        title: String = "温馨提示",
        content: String? = null,
        toolbar: @Composable () -> Unit
    ) {
        this.title = title
        this.content = content
        this.subContent = null
        this.toolbar = toolbar
        visible = true
    }

    fun dismiss() {
        visible = false
    }
}

@Composable
fun alertStockButton(
    text: String,
    contentColor: Color = CCColor.f1,
    onClick: () -> Unit,
) {
    CCButton(
        onClick = onClick,
        backgroundColor = Color.Transparent,
        contentColor = contentColor,
        modifier = Modifier
            .clip(CircleShape)
            .border(0.5.dp, contentColor, CircleShape),
    ) {
        Text(
            text = text,
            style = CCFont.f2.v1,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun alertFullButton(
    text: String,
    backgroundColor: Color = CCColor.f1,
    contentColor: Color = CCColor.b1,
    onClick: () -> Unit,
) {
    CCButton(
        onClick = onClick,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier.clip(CircleShape),
    ) {
        Text(
            text = text,
            style = CCFont.f2.v1,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun CCNormalAlert(
    vm: CCNormalAlertViewModel = viewModel()
) {
    XMNormalAlert(
        visible = vm.visible,
        onDismiss = { },
        title = vm.title,
        content = vm.content,
        subContent = vm.subContent,
    ) {
        vm.toolbar?.let { it() }
    }
}

@Composable
private fun XMNormalAlert(
    visible: Boolean,
    onDismiss: () -> Unit,
    title: String = "",
    content: String? = null,
    subContent: String? = null,
    toolbar: @Composable ColumnScope.() -> Unit
) {
    CCAlert(visible = visible, onDismiss = onDismiss, modifier = Modifier.fillMaxWidth(0.816f)) {
        Text(
            text = title,
            style = CCFont.big3b.v1.f1c
        )
        /// 内容
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            if (!content.isNullOrEmpty()) {
                Text(
                    text = content,
                    style = CCFont.f2.v1.alignCenter.f1c
                )
            }
            if (!subContent.isNullOrEmpty()) {
                Text(
                    text = subContent,
                    style = CCFont.f2.v1.alignCenter.f2c
                )
            }
        }
        /// 按钮
        toolbar()
    }
}

@Preview
@Composable
private fun preview() {
    PreviewScreen { }
    val vm: CCNormalAlertViewModel = viewModel()
    vm.show(
        leftTitle = "取消",
        content = randomString(30)
    )
    CCNormalAlert()
}