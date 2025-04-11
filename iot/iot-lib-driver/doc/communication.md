# Communication

Drivers communicate solely with dispatchers:

| Category      | Direction            | Communication method          |
|---------------|----------------------|-------------------------------|
| requests      | dispatcher to driver | transient, service-call based |
| announcements | driver to dispatcher | persistent, queue based       |
| histories     | driver to dispatcher | persistent, queue based       |

## Requests

- instruct the driver to perform an operation like reading, writing or configuring a point
- are received by the `AioDriverService` which serves `AioDriverApi`
- are synchronous, return of the service call indicates that the request has been received and/or executed by the driver
- are transient, if the request does not reach the driver it will return with an error to the requester

## Announcements

- created by the driver to announce changes such as new point values or configuration changes
- are persistent, saved to the disk into a queue
- are sent to the dispatcher, if the dispatcher is not available, the driver tries again in a short time

