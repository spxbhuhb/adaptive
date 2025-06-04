package `fun`.adaptive.ui.support.snapshot

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.fragment.layout.SizingProposal

@Adat
class FragmentSnapshot(
    val key: String,
    val name : String?,
    val state: List<Any?>,
    val children: List<FragmentSnapshot>,
    var finalTop: Double?,
    var finalLeft: Double?,
    var finalWidth: Double?,
    var finalHeight: Double?,
    var sizingProposal: SizingProposal?
)