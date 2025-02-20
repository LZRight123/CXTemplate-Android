package com.fantasy.components.widget

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fantasy.components.animations.cxScaleIn
import com.fantasy.components.extension.b1c
import com.fantasy.components.extension.mockImage
import com.fantasy.components.theme.CXColor
import com.fantasy.components.theme.CXFont
import com.skydoves.landscapist.ImageOptions
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable

class ImageViewerViewModel : ViewModel() {
    var images by mutableStateOf<List<Any>?>(null)
        private set
    var initialIndex by mutableIntStateOf(0)
        private set

    fun show(images: List<Any>, atIndex: Int = 0) {
        this.images = images
        initialIndex = atIndex
    }

    fun show(image: Any) {
        show(listOf(image))
    }

    fun dismiss() {
        images = null
    }
}

@Composable
fun ImageViewer(
    vm: ImageViewerViewModel = viewModel()
) {
    AnimatedVisibility(
        visible = vm.images != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        CXFullscreenPopup(
            onSystemBack = { vm.dismiss() }
        ) {

            val images = remember { vm.images ?: emptyList() }
            val pageState = rememberPagerState(vm.initialIndex) {
                images.size
            }
            Box(
                modifier = Modifier
                    .background(CXColor.b1.copy(0.97f))
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                val zoomState = rememberZoomableState()
                HorizontalPager(
                    state = pageState,
                    modifier = Modifier
                        .cxScaleIn(
                            show = vm.images != null,
                            duration = if (vm.images != null) 300 else 100
                        )
                        .fillMaxSize(),
                ) { page ->
                    if (pageState.settledPage != page) {
                        LaunchedEffect(key1 = Unit) {
                            zoomState.resetZoom(false)
                        }
                    }
                    CXCoilImage(
                        model = { images[page] },
                        imageOptions = ImageOptions(contentScale = ContentScale.FillWidth),
                        modifier = Modifier
                            .zoomable(
                                state = zoomState,
                                onClick = {
                                    vm.dismiss()
                                }
                            )
                            .fillMaxSize()
                    )
                }

                if (images.size > 1) {
                    Text(
                        text = "${pageState.currentPage + 1}/${images.size}",
                        style = CXFont.f1.v1.b1c,
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(top = 12.dp)
                            .background(CXColor.f1.copy(0.4f), RoundedCornerShape(8.dp))
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )

                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ImageViewerPreview() {
    PreviewScreen {

    }
    val vm: ImageViewerViewModel = viewModel()
    vm.apply { show((0..20).map { mockImage }) }
    ImageViewer()
}
