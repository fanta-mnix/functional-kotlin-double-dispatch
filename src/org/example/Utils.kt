package org.example

fun <T1, T2, R> ((T1, T2) -> R).curry(instance: T1): (T2) -> R = { p: T2 -> this(instance, p) }