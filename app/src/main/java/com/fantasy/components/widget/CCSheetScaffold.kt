package com.fantasy.components.widget

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.fantasy.components.extension.f1c
import com.fantasy.components.extension.f2c
import com.fantasy.components.extension.randomString
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.navBarHeight
import com.fantasy.components.tools.screenWidth
import okhttp3.internal.http2.Header

@Composable
fun CCPageHeader(
    title: String = ""
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 24.dp)
            .requiredWidth(screenWidth.dp)
    ) {
        Text(text = title, style = CCFont.f1b.v2.f1c)
        CCHLine()
    }
}

@Composable
fun CCSheetScaffold(
    dragHandler: @Composable () -> Unit = { }, // CCCapsuleIndicator(modifier = Modifier.padding(bottom = 8.dp))
    shape: Shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
    isShowLoading: Boolean = false,
    backgroundColor: Color = CCColor.b1,
    title: String = "",
    pageHeader: @Composable () -> Unit = {
        if (title.isNotEmpty()) {
            CCPageHeader(title)
        }
    },
    bottomBar: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = 12.dp + 6.dp)
            .fillMaxWidth()
    ) {
        dragHandler()
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .clip(shape)
                    .background(backgroundColor),
            ) {
                pageHeader()
                content()
            }


            Box(modifier = Modifier.matchParentSize()) {
                CCLoading(isShow = isShowLoading)
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                bottomBar()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen() {
        Spacer(modifier = Modifier.weight(1f))

        CCSheetScaffold(
            title = randomString(4)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp))
        }

        CCSheetScaffold {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp))
        }
    }
}