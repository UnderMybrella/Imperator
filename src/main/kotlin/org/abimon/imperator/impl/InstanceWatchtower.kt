package org.abimon.imperator.impl

import org.abimon.imperator.handle.Order
import org.abimon.imperator.handle.Watchtower

open class InstanceWatchtower<in T: Order>(private val t: Class<T>, private val name: String, val additionalRegulation: (T) -> Boolean = { true }): Watchtower {
    companion object {
        inline operator fun <reified T : Order> invoke(name: String = "Watching out for orders of type ${T::class.simpleName}") = InstanceWatchtower(T::class.java, name)
        inline operator fun <reified T : Order> invoke(name: String = "Watching out for orders of type ${T::class.simpleName}", noinline additionalRegulation: (T) -> Boolean) = InstanceWatchtower(T::class.java, name, additionalRegulation)
    }

    override fun getName(): String = name

    @Suppress("UNCHECKED_CAST")
    override fun allow(order: Order): Boolean = t.isInstance(order) && additionalRegulation.invoke(order as T)
}