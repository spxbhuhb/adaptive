# Things to remember

## IrWhen return value

Only works when the `when` is exhaustive. Add an `else` branch to the end.

## Check JS code sizes

`npx source-map-explorer site-app.js site-app.js.map`