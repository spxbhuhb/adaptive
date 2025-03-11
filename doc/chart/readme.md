# Chart

## Rendering

Chart rendering uses a canvas. The `placeLayout` of the canvas fragment.

The canvas fragment passes the size of the canvas to its children and the chart typically uses this size.

Input data for rendering:

- canvas size
- axes
- series

## Normalization

For cartesian charts (that rely on X-Y pairs) the value and marker calculations are executed on normalized values.

Normalization converts whatever value the chart handles into a `Double` in the `[0, 1)` range. Calculations and
positioning use these normalized values and the drawing fragments convert them back to canvas coordinates.
