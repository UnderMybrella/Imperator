package org.abimon.imperator.impl

import org.abimon.imperator.handle.Order
import org.abimon.imperator.handle.Watchtower

open class BasicWatchtower(private val name: String, val regulations: (Order) -> Boolean): Watchtower {
    override fun allow(order: Order): Boolean = regulations.invoke(order)

    override fun getName(): String = name

}