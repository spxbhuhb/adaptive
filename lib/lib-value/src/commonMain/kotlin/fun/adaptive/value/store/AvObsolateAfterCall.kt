package `fun`.adaptive.value.store

/**
 * This annotation means that the annotated parameter value becomes
 * obsoleted after the call and should be got from the store again
 * to have an up-to-date version.
 */
annotation class AvObsoleteAfterCall()
