package org.abimon.imperator.impl

import org.abimon.imperator.handle.Order
import org.abimon.imperator.handle.Soldier
import org.abimon.imperator.handle.Watchtower
import java.util.*

open class InstanceSoldier<in T: Order>(t: Class<T>, private val name: String, private val watchtowers: ArrayList<Watchtower>, val turn: (T) -> Unit): Soldier {
    companion object {
        inline operator fun <reified T : Order> invoke(name: String = "Watching out for orders of type ${T::class.simpleName}", watchtowers: Collection<Watchtower>, noinline turn: (T) -> Unit)
                = InstanceSoldier(T::class.java, name, ArrayList(watchtowers), turn)

        inline operator fun <reified T : Order> invoke(name: String = "Watching out for orders of type ${T::class.simpleName}", watchtower: Watchtower, noinline turn: (T) -> Unit)
                = InstanceSoldier(T::class.java, name, ArrayList(Collections.singleton(watchtower)), turn)
    }

    override fun getWatchtowers(): Collection<Watchtower> = watchtowers

    @Suppress("UNCHECKED_CAST")
    override fun command(order: Order) = turn.invoke(order as T)

    override fun getName(): String = name

    init {
        watchtowers.add(InstanceWatchtower(t, "Watching out for orders of type ${t.simpleName}"))
    }
}