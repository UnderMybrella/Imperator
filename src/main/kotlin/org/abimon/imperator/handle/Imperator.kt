package org.abimon.imperator.handle

import java.util.*

/**
 * Here we go. It's the Big Bad Imperator.
 *
 * The [Imperator] class is responsible for maintaining a list of [Scout][Scout]s, and listening to their orders.
 * Then, those orders must be filtered for the [Soldier][Soldier]s, checking against their [Watchtower][Watchtower]s, and then commanding them.
 *
 * Just like [Order]s may be considered events, an [Imperator] may be considered an event *dispatcher*, checking the events before they're sent.
 *
 * [Imperator]s are also responsible for accepting [Spies][Spy], who are responsible for transforming [Order][Order]s however they desire. These should be monitored ***very*** carefully.
 * [Scout]s do not receive the opportunity to add [Announcement]s until *after* the [Spies][Spy] have had their fun
 */
interface Imperator {
    fun dispatch(originalOrder: Order)

    fun hireScout(scout: Scout)
    fun fireScout(scout: Scout): Optional<Scout>
    fun fireScoutByName(spyName: String): Optional<Scout>
    fun fireAllScouts()

    fun hireSoldier(soldier: Soldier)
    fun fireSoldier(soldier: Soldier): Optional<Soldier>
    fun fireSoldierByName(soldierName: String): Optional<Soldier>
    fun fireAllSoldiers()

    /**
     * Hires a [Spy] to join the ranks. [priority] is the priority of this spy, and determines the position of the [spy][Spy] in the chain of intrigue.
     * A lower priority means it will modofiy the
     */
    fun hireSpy(spy: Spy, priority: Int = 0)
    fun fireSpy(spy: Spy): Optional<Spy>
    fun fireSpyByName(spyName: String): Optional<Spy>
    fun fireAllSpies()

    fun getScouts(): List<Scout>
    fun getSoldiers(): List<Soldier>
    fun getSpies(): List<Spy>
}