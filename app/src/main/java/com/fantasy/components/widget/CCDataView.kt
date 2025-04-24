package com.fantasy.components.widget


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
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
fun CCDataView(
    isLoading: Boolean = false,
    isError: Boolean = false,
    errorView: @Composable () -> Unit = {},
    loadingView: @Composable () -> Unit = {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(2) {
                CCText(
                    text = null,
                    length = 12
                )
            }
        }
    },
    content: @Composable () -> Unit
) {
    val sate = if (isLoading) 0 else if (isError) 1 else 2
    AnimatedContent(
        targetState = sate,
        transitionSpec = {
            scaleIn(initialScale = 0.9f) + fadeIn() togetherWith fadeOut()
        }
    ) {
        when (it) {
            0 -> loadingView()
            1 -> errorView()
            2 -> content()
        }
    }

}

@Preview
@Composable
private fun preview() {
    PreviewScreen {
        CCDataView() {
            Text(text = randomString(20))
        }

        CCDataView(isLoading = true) {
            Text(text = randomString(20))
        }
    }

}