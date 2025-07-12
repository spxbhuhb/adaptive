# Chart

[example](actualize://example-group?name=chart)

## Rendering

Chart rendering uses a canvas. `placeLayout` of the canvas fragment passes the size of the canvas to its 
children and the chart typically uses this size.

Input data for rendering:

- canvas size
- axes
- series

## Cartesian charts

Cartesian charts use an X and an Y axis to display pairs of values. The type of the values
are generic, the code uses `XT` and `YT`, both types has to be `Comparable`.

Processing steps:

1. calculating the data range by using `ChartDataRange.update`
2. normalize the data by using `ChartItem.normalize`
3. convert the normalized data into render operations by using `ChartItem.lineTo`

Normalization converts whatever types the chart handles into a `Double`. Calculations and
positioning use these normalized values and the drawing fragments convert them back to canvas coordinates.

Dependencies:

| Step          | Source data | Normalized data | Canvas Size |
|---------------|-------------|-----------------|-------------|
| 1. range      | yes         | no              | no          |
| 2. normalize  | yes         | no              | no          |
| 3. operations | no          | yes             | yes         |

### Data model

- `ChartDataRange`:
  - the minimum and maximum values for `XT` and `YT` for the whole chart
- `ChartItem`:
  - source data (list of `ChartDataPoint<XY,YT>`)
  - normalized data
  - operations
  - rendering fragment key
  - instructions (such as color) for the rendering fragment
- `ChartAxis`
  - definition of an axis to render
  - `ChartMarker`
    - a marker (tick, label, guideline) on the axis

### Controllers

Chart controllers typically support operations such as:

- add series
- remove series
- zoom in
- zoom out

#### Add series

When adding series to a chart:

- the controller typically downloads the data from a data provider
- the download is asynchronous, the chart is updated when the data arrives

Update:

- update the range with the new series
- if the updated range is larger than the previous one:
  - re-normalize all series
  - convert all series into render operations
  - recalculate axes
- otherwise
  - normalize the new series
  - convert the new series into render operations
- redraw the canvas (execute all render operations)
