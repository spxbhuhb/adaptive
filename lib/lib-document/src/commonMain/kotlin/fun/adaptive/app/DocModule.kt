package `fun`.adaptive.app

import `fun`.adaptive.document.model.*
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

class DocModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW, BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + DocBlockFragment
        + DocBlockImage
        + DocCodeFence
        + DocHeader
        + DocInlineFragment
        + DocInlineImage
        + DocLink
        + DocList
        + DocListItem
        + DocParagraph
        + DocQuote
        + DocRule
        + DocStyle
        + DocText
    }

}