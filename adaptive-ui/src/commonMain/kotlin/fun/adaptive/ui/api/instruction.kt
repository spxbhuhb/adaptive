package `fun`.adaptive.ui.api

import `fun`.adaptive.foundation.instruction.AdaptiveDetach
import `fun`.adaptive.foundation.instruction.DetachHandler
import `fun`.adaptive.foundation.instruction.DetachName
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.resource.FileResource
import `fun`.adaptive.ui.instruction.layout.GridColSpan
import `fun`.adaptive.ui.instruction.layout.GridColTemplate
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.text.FontName
import `fun`.adaptive.ui.instruction.text.FontSize
import `fun`.adaptive.ui.instruction.text.FontWeight
import `fun`.adaptive.ui.instruction.text.LetterSpacing
import `fun`.adaptive.ui.instruction.text.LineHeight
import `fun`.adaptive.ui.instruction.text.NoSelect
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.instruction.text.TextColor
import `fun`.adaptive.ui.instruction.text.TextSmallCaps
import `fun`.adaptive.ui.instruction.text.TextUnderline
import `fun`.adaptive.ui.instruction.text.TextWrap
import `fun`.adaptive.ui.instruction.navigation.ExternalLink
import `fun`.adaptive.ui.instruction.navigation.HistorySize
import `fun`.adaptive.ui.instruction.navigation.NavClick
import `fun`.adaptive.ui.instruction.navigation.Route
import `fun`.adaptive.ui.instruction.layout.GridCol
import `fun`.adaptive.ui.instruction.layout.GridPos
import `fun`.adaptive.ui.instruction.layout.GridRow
import `fun`.adaptive.ui.instruction.layout.GridRepeat
import `fun`.adaptive.ui.instruction.layout.GridRowSpan
import `fun`.adaptive.ui.instruction.layout.GridRowTemplate
import `fun`.adaptive.ui.instruction.layout.GridTrack
import `fun`.adaptive.ui.instruction.layout.FlowItemLimit
import `fun`.adaptive.ui.instruction.decoration.BackgroundColor
import `fun`.adaptive.ui.instruction.decoration.BackgroundGradient
import `fun`.adaptive.ui.instruction.decoration.Border
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.decoration.CornerRadius
import `fun`.adaptive.ui.instruction.decoration.DropShadow
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.instruction.event.OnClose
import `fun`.adaptive.ui.instruction.event.OnMove
import `fun`.adaptive.ui.instruction.event.OnPrimaryDown
import `fun`.adaptive.ui.instruction.event.OnPrimaryUp
import `fun`.adaptive.ui.instruction.event.OnSecondaryDown
import `fun`.adaptive.ui.instruction.event.OnSecondaryUp
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.input.InputPlaceholder
import `fun`.adaptive.ui.instruction.input.TabIndex
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.layout.AlignSelf
import `fun`.adaptive.ui.instruction.layout.DistributeSpace
import `fun`.adaptive.ui.instruction.layout.Fixed
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Gap
import `fun`.adaptive.ui.instruction.layout.Height
import `fun`.adaptive.ui.instruction.layout.Margin
import `fun`.adaptive.ui.instruction.layout.MaxHeight
import `fun`.adaptive.ui.instruction.layout.MaxSize
import `fun`.adaptive.ui.instruction.layout.MaxWidth
import `fun`.adaptive.ui.instruction.layout.Padding
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.Scroll
import `fun`.adaptive.ui.instruction.layout.Size
import `fun`.adaptive.ui.instruction.layout.SpaceDistribution
import `fun`.adaptive.ui.instruction.layout.Width
import `fun`.adaptive.ui.instruction.layout.ZIndex
import `fun`.adaptive.ui.instruction.text.ToText

// ------------------------------------------------------------------------------------
// Decoration
// ------------------------------------------------------------------------------------

fun backgroundColor(color: Color) = BackgroundColor(color)
fun backgroundColor(color: Int, opacity: Float = 1f) = BackgroundColor(Color(color.toUInt(), opacity))
fun backgroundColor(color: UInt, opacity: Float = 1f) = BackgroundColor(Color(color, opacity))

inline fun backgroundColor(color: () -> Int) = BackgroundColor(Color(color().toUInt()))

fun backgroundGradient(startPosition: Position, endPosition: Position, start: Color, end: Color) = BackgroundGradient(startPosition, endPosition, start, end)
fun leftToRightGradient(leftColor: Color, rightColor: Color) = BackgroundGradient(BackgroundGradient.LEFT, BackgroundGradient.RIGHT, leftColor, rightColor)

fun border(color: Color, width: DPixel = 1.dp) = Border(color, width, width, width, width)
fun border(color: Color, top: DPixel = 1.dp, right: DPixel = 1.dp, bottom: DPixel = 1.dp, left: DPixel = 1.dp ) = Border(color, top, right, bottom, left)
fun borderBottom(color: Color, width: DPixel = 1.dp) = Border(color, null, null, width, null)
fun borderLeft(color: Color, width: DPixel = 1.dp) = Border(color, null, null, null, width)
fun borderRight(color: Color, width: DPixel = 1.dp) = Border(color, null, width, null, null)
fun borderTop(color: Color, width: DPixel = 1.dp) = Border(color, width, null, null, null)

val noBorder = Border(color(0u), 0.dp, null, null, null)

fun color(color: Int, opacity: Float = 1f) = Color(color.toUInt(), opacity)
fun color(color: UInt, opacity: Float = 1f) = Color(color, opacity)

fun cornerRadius(all: DPixel) = CornerRadius(all)
fun cornerRadius(topLeft: DPixel? = null, topRight: DPixel? = null, bottomLeft: DPixel? = null, bottomRight: DPixel? = null) = CornerRadius(topLeft, topRight, bottomLeft, bottomRight)

fun cornerBottomLeftRadius(bottomLeft: DPixel) = cornerRadius(bottomLeft = bottomLeft)
fun cornerBottomRadius(bottom: DPixel) = cornerRadius(bottomLeft = bottom, bottomRight = bottom)
fun cornerBottomRightRadius(bottomRight: DPixel) = cornerRadius(bottomRight = bottomRight)
fun cornerTopLeftRadius(topLeft: DPixel) = cornerRadius(topLeft = topLeft)
fun cornerTopRadius(top: DPixel) = cornerRadius(topLeft = top, topRight = top)
fun cornerTopRightRadius(topRight: DPixel) = cornerRadius(topRight = topRight)

fun dropShadow(color: Color, offsetX: DPixel, offsetY: DPixel, standardDeviation: DPixel) = DropShadow(color, offsetX, offsetY, standardDeviation)

// ------------------------------------------------------------------------------------
// Event
// ------------------------------------------------------------------------------------

fun onClick(handler: (event: UIEvent) -> Unit) = OnClick(handler)

fun onMove(handler: (event: UIEvent) -> Unit) = OnMove(handler)

fun onPrimaryDown(handler: (event: UIEvent) -> Unit) = OnPrimaryDown(handler)
fun onPrimaryUp(handler: (event: UIEvent) -> Unit) = OnPrimaryUp(handler)

fun onSecondaryDown(handler: (event: UIEvent) -> Unit) = OnSecondaryDown(handler)
fun onSecondaryUp(handler: (event: UIEvent) -> Unit) = OnSecondaryUp(handler)

fun onClose(handler: () -> Unit) = OnClose(handler)

// ------------------------------------------------------------------------------------
// Layout
// ------------------------------------------------------------------------------------

fun frame(top: DPixel, left: DPixel, width: DPixel, height: DPixel) = Frame(top, left, width, height)

fun position(top: DPixel, left: DPixel) = Position(top, left)
fun size(width: DPixel, height: DPixel) = Size(width, height)

fun height(height: DPixel) = Height(height)
fun height(calc: () -> DPixel) = Height(calc())

fun width(width: DPixel) = Width(width)
fun width(calc: () -> DPixel) = Width(calc())

val maxSize = MaxSize()
val maxWidth = MaxWidth()
val maxHeight = MaxHeight()

fun gap(calcBoth: () -> DPixel): Gap = calcBoth().let { Gap(it, it) }
fun gap(both: DPixel): Gap = Gap(both, both)
fun gap(width: DPixel? = null, height: DPixel? = null): Gap = Gap(width, height)
fun gapHeight(height: () -> DPixel): Gap = Gap(height = height(), width = null)
fun gapHeight(height: DPixel): Gap = Gap(height = height, width = null)
fun gapWidth(width: () -> DPixel): Gap = Gap(width = width(), height = null)
fun gapWidth(width: DPixel): Gap = Gap(width = width, height = null)

fun padding(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null): Padding = Padding(top, right, bottom, left)
fun padding(all: () -> DPixel): Padding = Padding(all())
fun padding(all: DPixel): Padding = Padding(all)
fun paddingHorizontal(horizontal: DPixel): Padding = Padding(null, horizontal, null, horizontal)
fun paddingHorizontal(horizontal: () -> DPixel): Padding = horizontal().let { Padding(null, it, null, it) }
fun paddingVertical(vertical: DPixel): Padding = Padding(null, vertical, null, vertical)
fun paddingVertical(vertical: () -> DPixel): Padding = vertical().let { Padding(it, null, it, null) }
fun paddingTop(top: DPixel): Padding = padding(top = top)
fun paddingRight(right: DPixel): Padding = padding(right = right)
fun paddingBottom(bottom: DPixel): Padding = padding(bottom = bottom)
fun paddingLeft(left: DPixel): Padding = padding(left = left)
fun paddingTop(top: () -> DPixel): Padding = padding(top = top())
fun paddingRight(right: () -> DPixel): Padding = padding(right = right())
fun paddingBottom(bottom: () -> DPixel): Padding = padding(bottom = bottom())
fun paddingLeft(left: () -> DPixel): Padding = padding(left = left())

fun margin(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null): Margin = Margin(top, right, bottom, left)
fun margin(all: () -> DPixel): Margin = Margin(all())
fun margin(all: DPixel): Margin = Margin(all)
fun marginTop(top: DPixel): Margin = margin(top = top)
fun marginRight(right: DPixel): Margin = margin(right = right)
fun marginBottom(bottom: DPixel): Margin = margin(bottom = bottom)
fun marginLeft(left: DPixel): Margin = margin(left = left)
fun marginTop(top: () -> DPixel): Margin = margin(top = top())
fun marginRight(right: () -> DPixel): Margin = margin(right = right())
fun marginBottom(bottom: () -> DPixel): Margin = margin(bottom = bottom())
fun marginLeft(left: () -> DPixel): Margin = margin(left = left())

val alignSelf = AlignSelf.Companion
val alignItems = AlignItems.Companion
val spaceAround: DistributeSpace = DistributeSpace(SpaceDistribution.Around)
val spaceBetween: DistributeSpace = DistributeSpace(SpaceDistribution.Between)

val scroll: Scroll = Scroll(horizontal = true, vertical = true)
val verticalScroll: Scroll = Scroll(horizontal = true, vertical = true)
val horizontalScroll: Scroll = Scroll(horizontal = true, vertical = true)

val fixed = Fixed()

fun zIndex(value: Int): ZIndex = ZIndex(value)
fun zIndex(value: () -> Int): ZIndex = ZIndex(value())

fun flowItemLimit(limit: Int) = FlowItemLimit(limit)
fun flowItemLimit(limit: () -> Int) = FlowItemLimit(limit())

fun colTemplate(vararg tracks: GridTrack, extend : GridTrack? = null) = GridColTemplate(tracks, extend)
fun rowTemplate(vararg tracks: GridTrack, extend : GridTrack? = null) = GridRowTemplate(tracks, extend)

infix fun GridTrack.repeat(count: Int): GridRepeat = GridRepeat(count, this)

fun colSpan(span: Int) = GridColSpan(span)
fun rowSpan(span: Int) = GridRowSpan(span)

fun gridCol(col: Int, span: Int = 1) = GridCol(col, span)
fun gridRow(row: Int, span: Int = 1) = GridRow(row, span)

fun gridPos(row: Int, col: Int, rowSpan: Int = 1, colSpan: Int = 1) = GridPos(row, col, rowSpan, colSpan)

val Number.gridCol
    inline get() = GridCol(this.toInt(), 1)

val Number.gridRow
    inline get() = GridRow(this.toInt(), 1)

val Number.rowSpan
    inline get() = GridRowSpan(this.toInt())

val Number.colSpan
    inline get() = GridColSpan(this.toInt())

// ------------------------------------------------------------------------------------
// Navigation
// ------------------------------------------------------------------------------------

fun navClick(
    slotName: Name = Name.Companion.ANONYMOUS,
    @DetachName segment: String? = null,
    @AdaptiveDetach detachFun: (handler: DetachHandler) -> Unit
) = NavClick(slotName, segment, detachFun)

/**
 * Defines a route for `slot` fragments. See the navigation
 * tutorial for details.
 */
fun route(
    @DetachName segment: String? = null,
    @AdaptiveDetach detachFun: (handler: DetachHandler) -> Unit
) = Route(segment, detachFun)

/**
 * Sets history size for `slot` fragments. See the navigation
 * tutorial for details.
 */
inline fun historySize(sizeFun: () -> Int) = HistorySize(sizeFun())

fun externalLink(res: FileResource) = ExternalLink(res.uri)
fun externalLink(href: String) = ExternalLink(href)

// ------------------------------------------------------------------------------------
// Text
// ------------------------------------------------------------------------------------

fun fontName(fontName: String) = FontName(fontName)
inline fun fontName(fontName: () -> String) = FontName(fontName())

fun fontSize(fontSize: SPixel) = FontSize(fontSize)
inline fun fontSize(fontSize: () -> SPixel) = FontSize(fontSize())

fun fontWeight(weight: Int) = FontWeight(weight)
inline fun fontWeight(fontWeight: () -> Int) = FontWeight(fontWeight())

val thinFont = FontWeight(100)
val extraLightFont = FontWeight(200)
val lightFont = FontWeight(300)
val normalFont = FontWeight(400)
val mediumFont = FontWeight(500)
val semiBoldFont = FontWeight(600)
val boldFont = FontWeight(700)
val extraBoldFont = FontWeight(800)
val blackFont = FontWeight(900)

fun lineHeight(height: DPixel) = LineHeight(height)
inline fun lineHeight(height: () -> DPixel) = LineHeight(height())

val noSelect = NoSelect()
val noTextWrap = TextWrap(false)
val textWrap = TextWrap(true)
val underline = TextUnderline()
val smallCaps = TextSmallCaps()

fun letterSpacing(value: Double) = LetterSpacing(value)

fun textColor(value: Int) = TextColor(Color(value.toUInt()))
fun textColor(value: UInt) = TextColor(Color(value))
fun textColor(value: Color) = TextColor(value)

// FIXME toText should be linked with the instructed component and correctness of T should be checked
fun <T> toText(toTextFun: (T) -> String) = ToText(toTextFun)

// ------------------------------------------------------------------------------------
// Input
// ------------------------------------------------------------------------------------

/**
 * ```text
 * -1    not focusable
 * 0     in order in document source
 * N     before m > N but after 0
 * ```
 */
inline fun tabIndex(value: () -> Int) = TabIndex(value())
inline fun inputPlaceholder(valueFun: () -> String) = InputPlaceholder(valueFun())