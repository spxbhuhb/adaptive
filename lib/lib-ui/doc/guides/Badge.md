# Badge

[badge](def://?inline)

## Examples

[Common Badges](actualize:///cookbook/badge/example/common)

[Value badges](actualize:///cookbook/badge/example/value)

[Badges with contentPaneHeader](actualize:///cookbook/badge/example/content-pane)

[Badge input](actualize:///cookbook/badge/example/input)

## Details

Built-in badge-related fragments:

- [badge](fragment://) - basic badge
- [valueBadges](fragment://) - badges for a [value](def://)
- [contentPaneHeader](fragment://) - uses [valueBadges](fragment://)
- [badgeInput](fragment://) - input for badges
- [badgeEditor](fragment://) - editor for badges

You can associate badge names with icons and badge themes.

For example, "offline" can be registered with the error theme and some icon,
and then you do not have to specify these when using the [badge](fragment://),
but you can pass `true` in [useSeverity](paramter://badge).

When [useSeverity](paramter://badge) is true, the badge will look up the
name in [badgeThemeMap](property://BadgeTheme) and render the badge accordingly.

## Example code

[commonBadgeExample](example://)

---

[valueBadgeExample](example://)

---

[contentPaneBadgeExample](example://)

---

[badgeInputExample](example://)