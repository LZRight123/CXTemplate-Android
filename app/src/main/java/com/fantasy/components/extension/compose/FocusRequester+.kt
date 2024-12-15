package com.fantasy.components.extension.compose

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.SoftwareKeyboardController
import com.fantasy.components.tools.localDelay

/**
 * 在 Composable 传 LocalSoftwareKeyboardController.current
 */
suspend fun FocusRequester.registerFirstResponse(
    keyboard: SoftwareKeyboardController? = null,
    delayMillis: Long = 50
) {
    localDelay(delayMillis)
    keyboard?.show()
    requestFocus()
}

fun FocusRequester.requestFirstResponse(
    keyboard: SoftwareKeyboardController? = null
) {
    keyboard?.show()
    requestFocus()
}

fun FocusRequester.freeFirstResponse(
    keyboard: SoftwareKeyboardController? = null,
    focusManager: FocusManager? = null, //LocalFocusManager.current
) {
    focusManager?.clearFocus()
    keyboard?.hide()
    freeFocus()
}