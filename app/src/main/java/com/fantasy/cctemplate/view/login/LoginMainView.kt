package com.fantasy.cctemplate.view.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fantasy.components.base.BaseScreen
import com.fantasy.components.base.BaseViewModel
import com.fantasy.components.widget.CCScaffold


class LoginMainViewModel : BaseViewModel() {

}

class LoginMainView : BaseScreen() {
    @Composable
    override fun body() {
        CCScaffold(
        ) {  }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    LoginMainView().Content()
}