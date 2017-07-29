package org.abimon.imperator.handle

/**
 * The [Soldier] implementations are where magic begins to happen. These are the front lines - this is where [Order]s are processed, and [Announcement]s read out.
 */
interface Soldier {
    fun getWatchtowers(): Collection<Watchtower>
    fun command(order: Order)

    fun getName(): String
}