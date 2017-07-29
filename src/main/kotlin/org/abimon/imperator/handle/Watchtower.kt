package org.abimon.imperator.handle

/**
 * [Watchtower] implementations are, essentially, filters that are employed by [Soldier]s to make sure that they only listen to [Order]s that they want to.
 * Nothing too fancy here
 */
interface Watchtower {
    fun allow(order: Order): Boolean

    fun getName(): String
}