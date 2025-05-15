# Actual UI

Actual UI refers to the specific low-level user interface implementation offered by a given platform.

For example, browsers provide an actual UI that consists of the DOM and CSS; iOS has UIViews, Android has Views.

A single platform may have more than one Actual UI implementation (for instance, Swing 
and JavaFX on the JVM are different actual UIs).

Within any actual UI, there are typically leaf elements (such as a label or an input field) and container
elements (ones that can hold other elements). [Expect UI fragments](def://) and [dependent UI fragments](def://)
use these actual UI elements to render the user interface of the application.