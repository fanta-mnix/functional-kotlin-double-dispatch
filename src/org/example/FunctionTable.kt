package org.example

import java.util.*

class FunctionTable<T : Any, R>(base: FunctionEntry<T, R>, vararg overloads: FunctionEntry<out T, R>) {

    private val overloadsByClass = listOf(base, *overloads).associateBy { it -> it.clazz }

    operator fun invoke(parameter: T): R {

        @Suppress("UNCHECKED_CAST")
        tailrec fun resolveFunction(classHierarchyIterator: Iterator<Class<*>>): (T) -> R {
            if (!classHierarchyIterator.hasNext()) throw AssertionError()

            val nextClass = classHierarchyIterator.next()
            val resolved = overloadsByClass[nextClass]?.function ?: return resolveFunction(classHierarchyIterator)
            return resolved as (T) -> R
        }

        return resolveFunction(BreadthFirstClassHierarchyIterator(parameter.javaClass)).invoke(parameter)
    }


    private class BreadthFirstClassHierarchyIterator(first: Class<*>) : Iterator<Class<*>> {

        private val next: Queue<Class<*>> = queueOf(first)

        override fun hasNext(): Boolean = next.isNotEmpty()

        override fun next(): Class<*> =
            if (next.isEmpty()) throw NoSuchElementException()
            else next.poll().apply {
                superclass?.let { next.add(it) }
                interfaces?.let { next.addAll(it) }
            }

        private fun <T> queueOf(first: T): Queue<T> = ArrayDeque(Collections.singletonList(first))
    }

}