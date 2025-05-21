# Sizing proposal

A sizing proposal is a suggestion [ui container fragments](def://) give to the [ui fragments](def://)
they contain, so the contained [ui fragment](def://) can base its own layout calculation on the sizes
suggested in the proposal.

The proposal contains the minimum and maximum width and height the container suggests for the
contained fragment to take. Some fragments may rightfully ignore this suggestion, some others
may size themselves so that they fill the available space.

## See also

- [layout](def://)
- [ui container fragment](def://)
- [ui fragment](def://)