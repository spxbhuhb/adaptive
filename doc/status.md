# Status

The project is in **preview** status:

* Many-many things already work and even more don't.
* Changes happen on daily basis, no compatibility is maintained at this stage.
* I try to keep the documentation up-to-date, but no promises.

## Proof of Concepts

As of now, the very basic UI components are implemented and mostly working on all supported platforms:

* layouts
* text
* image
* click event handler
* basic formatting and styling

Services, Adat and other, non UI related parts should work on all platforms. I haven't tried
for a while to be honest, focusing on the UI.

That said, there are bugs and some functionality might break down time-to-time.

## Roadmap

In the close future I'll focus on these areas:

* support of basic UI features on all platforms
    * textual input fields
    * checkbox
* picture taking for Android and iOS
* QR code reading for Android and iOS
* file store for Android and iOS
* basic charts
* server (these already exist in other projects, I have to move it here and document it)
    * authentication
    * role based access control on server side
    * file upload / download
