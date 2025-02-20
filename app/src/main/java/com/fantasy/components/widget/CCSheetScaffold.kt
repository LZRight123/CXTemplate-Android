package com.fantasy.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.fantasy.components.theme.CCColor
import com.fantasy.components.tools.navBarHeight

@Composable
fun CCSheetScaffold(
    dragHandler: @Composable () -> Unit = {
        CCCapsuleIndicator(modifier = Modifier.padding(bottom = 8.dp))
    },
    shape: Shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
    showLoading: Boolean = false,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = navBarHeight - 12.dp)
            .fillMaxWidth()
    ) {
        dragHandler()
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.clip(shape),
            ) {
                content()
            }


            Box(modifier = Modifier.matchParentSize()) {
                CCLoading(isShow = showLoading)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen() {
        Spacer(modifier = Modifier.weight(1f))

        CCSheetScaffold {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp))
        }
    }
}