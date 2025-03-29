package `fun`.adaptive.cookbook.auth.ui.a

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.cookbook.auth.ui.account.AccountEditorData
import `fun`.adaptive.cookbook.auth.ui.account.accountEditor
import `fun`.adaptive.cookbook.check_circle
import `fun`.adaptive.cookbook.close
import `fun`.adaptive.cookbook.edit
import `fun`.adaptive.cookbook.support.cornerRadius8
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.datetime.instant
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.iconColors
import `fun`.adaptive.ui.theme.textSmall





@Adaptive
fun item(item: BasicAccountSummary) {
    val hover = hover()

    val background =
        when {
            hover == true -> backgrounds.surfaceHover
            else -> backgrounds.surface
        }

    grid {
        maxWidth .. height { 36.dp } .. alignItems.startCenter .. borders.outline
        paddingLeft { 32.dp } .. paddingRight { 16.dp } .. gap { 16.dp }

        colTemplate(
            1.fr,    // login
            1.fr,    // name
            1.fr,    // e-mail
            1.fr,    // phone
            80.dp,   // activated
            80.dp,   // locked
            140.dp,  // last login
            24.dp    // edit
        )

        gap { 8.dp }
        cornerRadius8
        background

//        text(item.login) .. maxWidth
//        text(item.name) .. maxWidth
//        text(item.email.ifEmpty { "(nincs email)" }) .. maxWidth .. emptyInst(item.email)
//        text(item.phone.ifEmpty { "(nincs telefonszám)" }) .. maxWidth .. emptyInst(item.phone)
        box {
            if (item.activated) {
                icon(Graphics.check_circle) .. iconColors.onSurfaceFriendly
            } else {
                icon(Graphics.close) .. iconColors.onSurfaceAngry
            }
        }
//        box {
//            if (item.locked) {
//                icon(Graphics.lock) .. iconColors.onSurfaceAngry
//            } else {
//                icon(Graphics.lock_open) .. iconColors.onSurfaceFriendly
//            }
//        }
        instant(item.lastLogin) .. maxWidth .. alignSelf.endCenter .. textSmall

        iconDialog(Graphics.edit, "Fiók szerkesztése") {
            accountEditor(
                AccountEditorData(
                    item.id,
                    item.login,
                    item.name,
                    item.email,
                    item.phone,
                    item.activated,
                    item.locked
                ),
                it
            )
        }
    }
}