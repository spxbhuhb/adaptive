/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.ir.util

/**
 * Use this annotation when there is a processing of higher order adaptive parameters.
 * As of now the code assumes direct lambdas everywhere, but it could be an anonymous
 * function or a KFunction as well. If we want to support them in the future it will
 * be beneficial to know the places to check.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class HigherOrderProcessing
