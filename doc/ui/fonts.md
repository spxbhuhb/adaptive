# Fonts

Font files have considerable size. Open Sans without any sub-setting is about 256 KB which is the
same magnitude as the whole compressed IoT client application with Kotlin, reactive UI, workspace,
basic charting, etc. (as I write this will change of course, but still).

## Browser

### Loading fonts from a cloud provider

Add the link to the fonts into `index.html`. Also, don't forget to check the font loading
magic at the end of `index.html` as it is needed in this case to ensure proper handling ofaccented characters.

```html

<link rel="preconnect" href="https://fonts.gstatic.com">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Open+Sans:ital,wght@0,300..800;1,300..800&display=swap" rel="stylesheet">
```

### Self-hosting the fonts

You can add the fonts to `adaptiveResources/fonts` and then reference them from the styles in `index.html`:

```html

<style>
    @font-face {
        font-family: 'Open Sans';
        font-style: normal;
        font-weight: 300 800;
        font-display: swap;
        src: url('/resources/fun.adaptive.ui.builtin/fonts/OpenSans_All.woff2') format('woff2');
    }
</style>
```

In this case you have to get the font files from somewhere (you can download most fonts from
Google fonts). If you have a TTF you can use `fonttools` or `woff2_compress` to convert it
into `woff2`, that will reduce the size by half.

I've used these commands for the built-in Open Sans font files:

```shell
pyftsubset OpenSans-VariableFont_wdth,wght.ttf \
  --output-file=OpenSans_All.woff2 \
  --flavor=woff2 \
  --layout-features='*' \
  --glyphs='*' \
  --no-hinting \
  --font-number=0 \
  --drop-tables+=DSIG
```

You can reduce the size further with the `unicodes` parameter if you are fine with missing some parts:

```shell
LATIN="U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+0304, U+0308, U+0329, U+2000-206F, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD"
LATIN_EXT="U+0100-02BA, U+02BD-02C5, U+02C7-02CC, U+02CE-02D7, U+02DD-02FF, U+0304, U+0308, U+0329, U+1D00-1DBF, U+1E00-1E9F, U+1EF2-1EFF, U+2020, U+20A0-20AB, U+20AD-20C0, U+2113, U+2C60-2C7F, U+A720-A7FF"

pyftsubset OpenSans-VariableFont_wdth,wght.ttf \
  --output-file=OpenSans_Latin_Ext.woff2 \
  --flavor=woff2 \
  --unicodes="$LATIN,$LATIN_EXT" \
  --layout-features='*' \
  --no-hinting \
  --drop-tables+=DSIG
```
