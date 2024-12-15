package com.fantasy.components.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import com.fantasy.components.theme.CXColor
import com.fantasy.components.theme.sheetBackgroundColor

@Composable
fun <T> CXModalSystemSheet(
    data: T?,
    onDataChange: (T?) -> Unit,
    cancelable: Boolean = true,
    content: @Composable ColumnScope.(T) -> Unit,
) {
    var lastNonNullData by remember { mutableStateOf(data) }
    DisposableEffect(data) {
        if (data != null) lastNonNullData = data
        onDispose {}
    }

    CXModalSystemSheet(
        visible = data != null,
        onVisibleChange = { visible ->
            if (visible) {
                onDataChange(lastNonNullData)
            } else {
                onDataChange(null)
            }
        },
        cancelable = cancelable
    ) {
        lastNonNullData?.let {
            content(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CXModalSystemSheet(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
    cancelable: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (LocalInspectionMode.current && visible) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
        return
    }

    val internalCancelable = remember { mutableStateOf(cancelable) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            if (it == SheetValue.Hidden && !internalCancelable.value) {
                return@rememberModalBottomSheetState false
            }
            true
        }
    )

    LaunchedEffect(visible, cancelable) {
        if (visible) {
            internalCancelable.value = cancelable
            sheetState.show()
        } else {
            internalCancelable.value = true
            sheetState.hide()
        }
    }

    LaunchedEffect(sheetState.currentValue, sheetState.targetValue) {
        if (sheetState.currentValue == sheetState.targetValue) {
            val newVisible = sheetState.isVisible
            if (newVisible != visible) {
                onVisibleChange(newVisible)
            }
        }
    }

    if (!visible && sheetState.currentValue == sheetState.targetValue && !sheetState.isVisible) {
        return
    }

    ModalBottomSheet(
        onDismissRequest = {
            onVisibleChange(false)
        },
        dragHandle = {},
        sheetState = sheetState,
//        shape = RectangleShape,
        containerColor = Color.Transparent,
        contentColor = CXColor.f1,
        scrimColor = CXColor.sheetBackgroundColor,
        contentWindowInsets = {
            WindowInsets(0, 0, 0, 0)
        },
        properties = ModalBottomSheetProperties(shouldDismissOnBackPress = cancelable),
    ) {
        content()
//            Column(
//                verticalArrangement = Arrangement.Bottom,
//                modifier = Modifier
//                    .fantasyClick(
//                        null,
//                        enabled = cancelable
//                    ) {
//                        scope.launch {
//                            sheetState.hide()
//                            onVisibleChange(false)
//                        }
//
//                    }
//                    .fillMaxSize()
//            ) {
//            }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen {
        CXModalSystemSheet(
            visible = true,
            onVisibleChange = {},
        ) {

        }

    }
}