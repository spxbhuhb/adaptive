package `fun`.adaptive.ui.support.snapshot

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.visitor.AdatClassVisitor

class SnapshotLayoutDumpVisitor : AdatClassVisitor<Unit, SnapshotLayoutDumpVisitor.VisitorData>() {

    class VisitorData(
        val indent : String = "  ",
        var level : Int = 0,
        var output : StringBuilder = StringBuilder()
    ) {
        operator fun plusAssign(s: String) {
            output.append(indent.repeat(level))
            output.appendLine(s)
        }
    }

    override fun visitInstance(instance: AdatClass, data: VisitorData) {
        instance as FragmentSnapshot

        data.level ++
        data += "${instance.key}    [${instance.name ?: ""}]    ${instance.finalTop}  ${instance.finalLeft}  ${instance.finalWidth}  ${instance.finalHeight}   <--- (${instance.sizingProposal})"
        for (child in instance.children) {
            visitInstance(child, data)
        }
        data.level--
    }

}