package com.fantasy.components.widget

import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import com.fantasy.components.extension.f1c
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.cclog
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.markdown.MarkdownParseOptions
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.RichText
import com.halilibo.richtext.ui.string.*

@Composable
fun CCMarkdown(
    content: String,
    style: TextStyle = CCFont.f1.v1.f1c,
    boldStyle: TextStyle = CCFont.f1b.v1.f1c,
    linkStyle: TextStyle = CCFont.f1b.v2.f1c,
    markdownParseOptions: MarkdownParseOptions = MarkdownParseOptions.Default,
    modifier: Modifier = Modifier,
    onLinkClicked: ((String) -> Unit)? = { cclog(it) }
) {
    ProvideTextStyle(value = style) {
        RichText(
            modifier = modifier,
            style = RichTextStyle.Default.copy(
                stringStyle = RichTextStringStyle.Default.copy(
                    boldStyle = SpanStyle(
                        fontFamily = boldStyle.fontFamily,
                        fontWeight = boldStyle.fontWeight,
                        fontSize = boldStyle.fontSize,
                        fontStyle = boldStyle.fontStyle,
                        color = boldStyle.color,
                    ),
                    linkStyle = SpanStyle(
                        fontFamily = linkStyle.fontFamily,
                        fontWeight = linkStyle.fontWeight,
                        fontSize = linkStyle.fontSize,
                        fontStyle = linkStyle.fontStyle,
                        color = linkStyle.color,
                    )
                )
            )
        ) {
            Markdown(
                content = content,
                markdownParseOptions = markdownParseOptions,
                onLinkClicked = onLinkClicked
            )
        }
    }

}