package `fun`.adaptive.markdown.transform

import `fun`.adaptive.ui.richtext.RichTextTheme

class MarkdownTransformConfig(
    val theme : RichTextTheme = RichTextTheme.DEFAULT,
    val codeFence : String = "lib:richtext:codefence",
    val paragraph : String = "lib:richtext:paragraph",
    val list : String = "lib:richtext:list",
    val quote : String = "lib:richtext:quote",
    val horizontalRule : String = "lib:richtext:hr"
)