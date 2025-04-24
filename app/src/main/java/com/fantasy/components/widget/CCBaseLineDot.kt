package com.fantasy.components.widget


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.base.BaseScreen
import com.fantasy.components.widget.CCScaffold
import com.fantasy.components.theme.CCFont
import com.fantasy.components.theme.CCColor
import com.fantasy.components.widget.PreviewScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fantasy.components.extension.randomString

@Composable
fun CCBaseLineDot(
    size: Int = 8,
    alignmentStyle: TextStyle = CCFont.big1b.v2
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .background(CCColor.f1)
                .size(size.dp)
        )
        Text(text = "", style = alignmentStyle)
    }
}

@Preview
@Composable
private fun preview() {
    PreviewScreen {
        Row {
            CCBaseLineDot()

            Text(text = randomString(32), style = CCFont.big1b.v2)
        }

        Row {
            CCBaseLineDot(
                alignmentStyle = CCFont.f1.v2
            )

            Text(text = randomString(32), style = CCFont.f1.v2)
        }

        Row {
            CCBaseLineDot()

            Text(text = randomString(32), style = CCFont.big1b.v2)
        }
    }
}