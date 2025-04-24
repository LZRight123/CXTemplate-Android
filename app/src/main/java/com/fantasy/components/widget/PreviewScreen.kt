package com.fantasy.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.fantasy.components.extension.ccShimmerTheme
import com.fantasy.components.theme.CCColor
import com.fantasy.components.tools.inPreview
import com.valentinilk.shimmer.LocalShimmerTheme
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication

@Composable
fun PreviewScreen(
    backgroundColor: Color = CCColor.b2,
    modifier: Modifier = Modifier.background(backgroundColor),
    verticalArrangement: Int = 0,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    previewContext: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val body = @Composable {
        Column(
            verticalArrangement = Arrangement.spacedBy(verticalArrangement.dp),
            horizontalAlignment = horizontalAlignment,
            modifier = modifier
                .fillMaxSize()
        ) {
            content()
        }
    }

    CompositionLocalProvider(
        LocalShimmerTheme provides ccShimmerTheme,
    ) {
        if (previewContext) {
            val context = LocalContext.current
            KoinApplication(
                application = {
                    androidContext(context)
                }
            ) {
                inPreview = true
                body()
            }
        } else {
            body()
        }
    }

}

@Composable
@Preview
private fun PreviewScreenPreview() {
    PreviewScreen {}
}
