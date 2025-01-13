package `fun`.adaptive.cookbook.ui.file

import `fun`.adaptive.cookbook.file.FileApi
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.button.api.button
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLAnchorElement

@Adaptive
fun fileRecipe() {
    column {
        button("Download") .. onClick {
            adapter().scope.launch {
                val fileName = getService<FileApi>(adapter().transport).download()

                val a = document.createElement("a") as HTMLAnchorElement
                a.href = "/adaptive/download/$fileName"
                a.download = fileName

                document.body?.appendChild(a)
                a.click()
                document.body?.removeChild(a)
            }
        }
    }
}