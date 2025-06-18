# Badge

[badge](def://?inline)

## Hard-coded examples

[Common Badges](actualize:///cookbook/badge/example/common)

[Badges for a value](actualize:///cookbook/badge/example/value)

[Badges with contentPaneHeader](actualize:///cookbook/badge/example/content-pane)

## Details

Built-in badge fragments:

- [badge](fragment://) - basic badge
- [valueBadges](fragment://) - badges for a [value](def://)
- [contentPaneHeader](fragment://) - uses [valueBadges](fragment://)

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