package com.fantasy.components.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.fantasy.components.extension.color
import com.fantasy.components.extension.compose.Icon
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.dropdownMenuDefaultWidth

@Composable
fun CCMenuRow(
    title: String,
    @DrawableRes icon: Int? = null,
    tint: Color = CCColor.f1,
    onClick: () -> Unit,
) {
    CCButton(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = title, style = CCFont.f1.v1.color(tint))
            Spacer(modifier = Modifier.weight(1f))
            icon?.let {
                Icon(id = icon, tint = tint)
            }
        }
    }
}

@Composable
private fun CCMenuContentWrap(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .background(CCColor.b1)
            .padding(vertical = 4.dp)
            .width(dropdownMenuDefaultWidth),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        content()
    }
}

@Composable
fun CCDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.background(CCColor.b1),
        offset = offset,
        properties = PopupProperties(
            focusable = true,
        )
    ) {
        CCMenuContentWrap {
            content()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    PreviewScreen {
        CCMenuContentWrap {
            CCMenuRow(
                title = "删除本餐",
                tint = CCColor.error
            ) {}
        }

        Spacer(modifier = Modifier.height(100.dp))

        CCMenuContentWrap {
            CCMenuRow(
                title = "这是",
                tint = CCColor.f1
            ) {}
            CCHLine()
            CCMenuRow(
                title = "删除",
                tint = CCColor.error
            ) {}
        }


    }
}
