package org.abimon.imperator.handle

/**
 * The [Spy] implementations are responsible for basic intrigue among the ranks. Similar to [Soldier]s, they have [Watchtowers][getWatchtowers], and then [fiddle] with the [Order]s
 */
interface Spy {
    fun getWatchtowers(): Collection<Watchtower>
    fun fiddle(order: Order): Order

    fun getName(): String
}