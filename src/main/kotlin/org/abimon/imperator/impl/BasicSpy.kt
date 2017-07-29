package org.abimon.imperator.impl

import org.abimon.imperator.handle.Order
import org.abimon.imperator.handle.Spy
import org.abimon.imperator.handle.Watchtower

open class BasicSpy(private val name: String, private val watchtowers: Collection<Watchtower>, val fiddle: (Order) -> Order): Spy {
    override fun getWatchtowers(): Collection<Watchtower> = watchtowers

    override fun fiddle(order: Order): Order = fiddle.invoke(order)

    override fun getName(): String = name
}