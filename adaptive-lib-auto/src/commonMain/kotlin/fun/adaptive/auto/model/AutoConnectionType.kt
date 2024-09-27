package `fun`.adaptive.auto.model

enum class AutoConnectionType {
    /**
     * Connect through a service that implements `AutoApi`. May be used between same peers in the
     * same process, but a direct connection might be a better.
     */
    Service,

    /**
     * Connect directly, without involvement of a service or a worker. This connection type needs
     * the peer backend in the same process.
     */
    Direct
}