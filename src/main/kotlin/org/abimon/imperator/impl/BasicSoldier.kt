package org.abimon.imperator.impl

import org.abimon.imperator.handle.Order
import org.abimon.imperator.handle.Soldier
import org.abimon.imperator.handle.Watchtower
import java.util.*

open class BasicSoldier(private val name: String, private val watchtowers: Collection<Watchtower>, val turn: (Order) -> Unit): Soldier {

    constructor(name: String, watchtower: Watchtower, turn: (Order) -> Unit) : this(name, Collections.singleton(watchtower), turn)

    override fun getWatchtowers(): Collection<Watchtower> = watchtowers

    override fun command(order: Order) = turn.invoke(order)

    override fun getName(): String = name
}