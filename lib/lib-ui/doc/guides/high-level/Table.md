---
status: review
markers: ai-generated
---

# Table

The [table](fragment://) fragment renders a list of items as a responsive grid of “cells.” It uses the [cellBox](fragment://)
layout to arrange cells per item, adapting to the available width. When there isn’t enough horizontal space, related cells can
collapse into vertical groups, and as a last resort the item is rendered as a vertical list of its cells.

Read first:

- [Cell box](guide://) (responsive layout and cell groups)
- [Fragments](guide://) (foundation, building blocks, reactivity, etc.)
- [Instructions](guide://) (placement, visibility, etc.)

## Examples

[examples](actualize://example-group?name=table)

## How it works

- You define a backend via `tableBackend { ... }` using [TableViewBackendBuilder](class://).
- Add cells (columns) with the provided builders. Each cell describes how to fetch and render one value from the item.
- Pass the backend to the [table](fragment://) fragment to render.
- The table uses the [Cell box](guide://) algorithm to compute an arrangement from cell definitions (minWidth/width) and cell groups.
- When space is limited, lower-priority groups collapse first; when no horizontal arrangement fits, the table falls back to a vertical item layout.

## Backend builder

Key properties and functions:
- items: List<ITEM>
- gap: Gap — spacing between cells (default 16.dp x 16.dp)
- numericWidth: Dp — default width/minWidth for numeric cells (int/double)
- theme: TableTheme — styling hooks (header, cell container, etc.)
- cellGroup(label: String, priority: Int): TableCellGroupDef — define a group to later assign to cell definitions.

Cell builders:
- stringCell { ... }
- intCell { ... }
- doubleCell { ... }
- iconCell { ... } — expects GraphicsResourceSet
- statusCell { ... } — expects Set<AvStatus> (any string status works with default matching)
- timeAgoCell { ... } — expects kotlin.time.Instant (not text-filterable, non-sortable)
- actionsCell { ... } — expects ActionIconRowBackend<T> for rendering action icons

Each builder configures a TableCellDefBuilder with these common options:
- label: String — header label
- width: GridTrack — preferred width (e.g., 1.fr)
- minWidth: Dp — minimum width used by the Cell box algorithm
- visible: Boolean — visibility toggle
- sortable: Boolean — enable sort (some cells disable it)
- resizable: Boolean — allow live resize (some cells disable it)
- supportsTextFilter: Boolean — include in text filtering (timeAgoCell disables)
- decimals: Int — numeric formatting precision (numeric cells)
- unit: String? — numeric unit appended (numeric cells)
- rowMenu: List<MenuItemBase<Any>> — per-row context menu
- headerInstructions: () → AdaptiveInstructionGroup — customize header cell rendering
- instructions: (ITEM) → AdaptiveInstructionGroup — customize cell container per item
- get: (ITEM) → CELL_DATA — extract cell data from the item (required)
- matchFun: (CELL_DATA, filterText: String) → Boolean — custom text filter matcher
- role: UUID<*>? — feature toggling via roles
- group: TableCellGroupDef? — assign to a group created with `cellGroup`

Notes:
- Numeric cells (int/double) default to right-aligned header and content and adopt `numericWidth` for width/minWidth.
- iconCell, statusCell, timeAgoCell, and actionsCell are non-resizable and/or non-sortable by design.

## Rendering

- [table](fragment://):
    - Renders an optional header row (when the arrangement is horizontal).
    - Renders items using `cellBox` with the computed arrangement.
    - Automatically switches to vertical item layout when needed.