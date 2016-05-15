package org.example

data class FunctionEntry<T, out R>(val clazz: Class<T>, val function: (T) -> R) {
    companion object {
        inline fun <reified T : Any, R> of(crossinline f: (T) -> R) = FunctionEntry(T::class.java, { x: T -> f(x) })
    }
}

