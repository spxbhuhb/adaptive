# Adapter

[adapter](def://?inline)

Built-in adapters:

- [AdaptiveAdapter](class://) - base class for all adapters
    - [BackendAdapter](class://) - adapter for [application backends](def://)
    - [DensityIndependentAdapter](class://) - base class rendering-type adapters
      - [AbstractAuiAdapter](class://) - adapter for [user interfaces](def://)
        - [AuiBrowserAdapter](class://) - UI adapter for browser
        - [AuiAdapter](class://) - UI adapter for iOS
        - [AuiAdapter](class://) - UI adapter for Android
      - [CanvasAdapter](class://) - adapter for drawing canvases
      - [SvgAdapter](class://) - adapter for SVG fragments
    - [AdaptiveTestAdapter](class://) - adapter for unit tests