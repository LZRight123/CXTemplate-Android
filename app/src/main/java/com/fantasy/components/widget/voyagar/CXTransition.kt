package com.fantasy.components.widget.voyagar

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.transitions.ScreenTransition

/**
 * https://voyager.adriel.cafe/transitions-api/
 */
@OptIn(ExperimentalVoyagerApi::class)
class CXFadeTransition : ScreenTransition {
    override fun enter(lastEvent: StackEvent): EnterTransition {
        return fadeIn(tween(1500))
    }

    override fun exit(lastEvent: StackEvent): ExitTransition {
        return fadeOut(tween(1500))
    }
}

@OptIn(ExperimentalVoyagerApi::class)
class CXSlideVertically : ScreenTransition {

    override fun enter(lastEvent: StackEvent): EnterTransition {
        return slideIn { size ->
            val y = if (lastEvent == StackEvent.Pop) -size.height else size.height
            IntOffset(x = 0, y = y)
        }
    }

    override fun exit(lastEvent: StackEvent): ExitTransition {
        return slideOut(
            animationSpec = tween(1500, 200)
        ) { size ->
            val y = if (lastEvent == StackEvent.Pop) size.height else -size.height
            IntOffset(x = 0, y = y)
        }
    }
}
