package com.fantasy.components.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.fantasy.components.theme.CCColor

fun TextStyle.color(color: Color) = copy(color = color)
fun TextStyle.size(size: Int) = copy(fontSize = size.sp)
val TextStyle.alignCenter get() = copy(textAlign = TextAlign.Center)
val TextStyle.alignEnd get() = copy(textAlign = TextAlign.End)
val TextStyle.f1c @Composable
get() = color(CCColor.f1)

val TextStyle.f2c @Composable
get() = color(CCColor.f2)

val TextStyle.f3c @Composable
get() = color(CCColor.f3)

val TextStyle.white @Composable
get() = color(CCColor.white)

val TextStyle.b1c @Composable
get() = color(CCColor.b1)

val TextStyle.b2c @Composable
get() = color(CCColor.b2)

val TextStyle.b3c @Composable
get() = color(CCColor.b3)

val TextStyle.errorc @Composable
get() = color(CCColor.error)

val TextStyle.boldBlack @Composable
get() = copy(fontWeight = FontWeight.Black)

val TextStyle.underline @Composable
get() = copy(textDecoration = TextDecoration.Underline)