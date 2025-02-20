package com.fantasy.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import com.fantasy.components.extension.compose.fantasyClick
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.sheetBackgroundColor
import com.fantasy.components.tools.hideKeyboard
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet

/**
 * 单独在一块屏幕上
 */
@OptIn(ExperimentalSheetApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CCModalSheet(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
    cancelable: Boolean = true,
    onSystemBack: (() -> Unit)? = { onVisibleChange(false) },
    scrimColor: Color = CCColor.sheetBackgroundColor,
    content: @Composable BoxScope.() -> Unit,
) {
    if (LocalInspectionMode.current && visible) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
        return
    }

    ModalSheet(
        visible = visible,
        onVisibleChange = {
            onVisibleChange(it)
        },
        onSystemBack = onSystemBack,
        elevation = 0.dp,
        backgroundColor = Color.Transparent,
        scrimColor = scrimColor,
        shape = BottomSheetDefaults.ExpandedShape,
        cancelable = cancelable,
    ) {
        Box(
            modifier = Modifier
                .fantasyClick(null) { }
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalSheetApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T> CCModalSheet(
    data: T?,
    onDataChange: (T?) -> Unit,
    cancelable: Boolean = true,
    onSystemBack: (() -> Unit)? = { onDataChange(null) },
    scrimColor: Color = CCColor.sheetBackgroundColor,
    content: @Composable BoxScope.(T) -> Unit,
) {
    if (LocalInspectionMode.current && data != null) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            content(data)
        }
        return
    }

    ModalSheet(
        data = data,
        onDataChange = {
            hideKeyboard()
            onDataChange(it)
        },
        onSystemBack = onSystemBack,
        elevation = 0.dp,
        backgroundColor = Color.Transparent,
        scrimColor = scrimColor,
        shape = BottomSheetDefaults.ExpandedShape,
        cancelable = cancelable,
    ) { d ->
        Box(
            modifier = Modifier.fantasyClick(null) { }
        ) {
            content(d)
        }
//        Box(
//            contentAlignment = Alignment.BottomCenter,
//            modifier = Modifier
//                .fantasyClick(null) { onDataChange(null) }
//                .fillMaxSize()
//        ) {
//
//        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen {
        CCModalSheet(
            visible = true,
            onVisibleChange = {}
        ) {
            Box(modifier = Modifier
                .background(CCColor.random)
                .fillMaxWidth()
                .height(300.dp))
        }
    }
}