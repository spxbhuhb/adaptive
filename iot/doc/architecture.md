# Architecture

An Adaptive IoT system is composed of nodes and drivers:

```text
top node (optional)
    +-- sub node
        +-- driver
        ...
        +-- driver
    ...
    +-- sub node
        +-- driver
        ...
        +-- driver
```

Nodes are responsible for:

- providing user interface
- storing configuration data
- maintaining historical data
- managing drivers
- communicating with drivers
- implementing logics

Drivers are responsible for:

- communicating with one (and only one) controller network
- performing network, controller and point related tasks
- maintaining the configuration of the network

## Data flow and ownership

There are two data flows between the components of an IoT system:

| Category      | Direction      | Communication method          |
|---------------|----------------|-------------------------------|
| requests      | node to driver | transient, service-call based |
| announcements | driver to node | persistent, queue based       |

### Requests

- instruct the driver to perform an operation like reading, writing or configuring a point
- are received by the `AioDriverService` which serves `AioDriverApi`
- are synchronous, return of the service call indicates that the request has been received and/or executed by the driver
- are transient, if the request does not reach the driver it will return with an error to the requester

### Announcements

- created by the driver to announce changes such as new point values or configuration changes
- are persistent, saved to the disk into a queue
- are sent to the dispatcher, if the dispatcher is not available, the driver tries again in a short time

### Data ownership

The conceptual owner of a given network and all data under the network is the driver. This means that the
domain specific part of network, controller and point items should be changed only by the driver.

When the user would like to change, let's say, the address of a ModBus device:

- the node sends a request to the driver to change the address
- the driver changes the address (or refuses the change)
- the driver announces to the address change
- the nodes update their own database with the change

This mechanism ensures that:

- the data can change only at one point (in the driver), where data consistency is enforced
- if a request fails (timeout, refused or other error), the user gets an immediate feedback
- the state of a network can be restored from the announcements at any given point