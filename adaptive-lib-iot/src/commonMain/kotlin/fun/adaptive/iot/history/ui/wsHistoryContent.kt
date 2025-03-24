package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.common.alarmIcon
import `fun`.adaptive.iot.common.localizedString
import `fun`.adaptive.iot.common.status
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.platform.download.downloadFile
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.utility.format
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.value.item.AvStatus

@Adaptive
fun wsHistoryContent(
    pane: WsPane<HistoryBrowserWsItem, HistoryContentController>
): AdaptiveFragment {

    val originalItem = copyOf { pane.data.item }.asAvItem<AioPointSpec>()
    val records = fetch {pane. controller.query(originalItem) } ?: emptyList()

    grid {
        maxSize .. padding { 16.dp } .. backgrounds.surface
        rowTemplate(56.dp, 46.dp, 1.fr)
        gap { 16.dp }

        title(originalItem, records)
        historyTableHeader()
        historyTable(records)
    }

    return fragment()
}


@Adaptive
fun title(
    item: AvItem<AioPointSpec>,
    records : List<AioDoubleHistoryRecord>
) {
    grid {
        maxSize
        colTemplate(1.fr, 1.fr)
        paddingBottom { 32.dp }

        column {
            h2(item.name.ifEmpty { Strings.noname })
            uuidLabel { item.uuid }
        }

        button(Strings.downloadReport) .. alignSelf.end .. onClick {
            downloadReport(records)
        }
    }
}

@Adaptive
fun historyTableHeader(
    theme: AioTheme = AioTheme.DEFAULT
) {
    grid {
        theme.itemListHeader .. maxSize .. paddingHorizontal { 8.dp }
        colTemplate(160.dp, 1.fr, 84.dp, 48.dp)

        text(Strings.timestamp) .. textMedium .. normalFont
        text(Strings.value) .. textMedium .. normalFont
        text(Strings.status) .. alignSelf.center .. textMedium .. normalFont
        text(Strings.alarm) .. textMedium .. normalFont
    }
}

@Adaptive
fun historyTable(
    records: List<AioDoubleHistoryRecord>,
    theme: AioTheme = AioTheme.DEFAULT
) {
    column {
        for (record in records) {
            grid {
                theme.itemListItemContainer .. maxSize .. paddingHorizontal { 8.dp }
                colTemplate(160.dp, 1.fr, 84.dp, 48.dp)

                timestamp(record.timestamp)
                text(record.value.format(1))
                status(AvStatus(record.flags))
                alarmIcon(record.flags)
            }
        }
    }
}

fun downloadReport(records: List<AioDoubleHistoryRecord>) {
    val out = StringBuilder()
    for (record in records) {
        out.append("${record.timestamp.localizedString()};${record.value};${record.flags}\n")
    }

    downloadFile(out.toString().encodeToByteArray(), "history.csv", "text/csv")
}