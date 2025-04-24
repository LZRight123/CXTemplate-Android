package com.fantasy.components.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fantasy.components.extension.f1c
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.CCFont


@Composable
fun CCTextFiled(
    value: String?,
    placeholder: String = "请输入...",
    textStyle: TextStyle = CCFont.f1.v1.f1c,
    placeholderStyle: TextStyle = textStyle.copy(color = CCColor.f1.copy(alpha = 0.3f)),
    innerTextFieldAlignment: Alignment = Alignment.TopStart,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    cursorBrush: Brush = SolidColor(CCColor.f1),
    leadingIcon: @Composable (RowScope.() -> Unit)? = null,
    trailingIcon: @Composable (RowScope.() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        value = value ?: "",
        modifier = modifier,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = cursorBrush,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        decorationBox = @Composable { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(IntrinsicSize.Max)
            ) {
                leadingIcon?.let { it() }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = when (textStyle.textAlign) {
                        TextAlign.End, TextAlign.Right -> Alignment.CenterEnd
                        else -> innerTextFieldAlignment
                    }
                ) {
                    Row {
                        AnimatedVisibility(
                            visible = value.isNullOrEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut(tween(20))
                        ) {
                            Text(
                                text = placeholder,
                                style = placeholderStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    innerTextField()
                }

                trailingIcon?.let {
                    it()
                }
            }

        })
}

@Composable
fun CCTextFiled(
    fieldValue: TextFieldValue,
    placeholder: String = "请输入",
    textStyle: TextStyle = CCFont.f2.v1.f1c,
    placeholderStyle: TextStyle = textStyle.copy(color = CCColor.f1.copy(alpha = 0.3f)),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    cursorBrush: Brush = SolidColor(CCColor.f1),
    leadingIcon: @Composable (RowScope.() -> Unit)? = null,
    trailingIcon: @Composable (RowScope.() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit,
) {
    CCTextFiled(
        value = fieldValue.text,
        placeholder = placeholder,
        textStyle = textStyle,
        placeholderStyle = placeholderStyle,
        enabled = enabled,
        readOnly = readOnly,
        cursorBrush = cursorBrush,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        modifier = modifier
    ) {
        // selection = TextRange(it.length)
        onValueChange(TextFieldValue(text = it))
    }
}

