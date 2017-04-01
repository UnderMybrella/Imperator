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

/**
 * An implementation of [Scout] is responsible for gathering information and relaying it to an [Imperator] instance.
 *
 * This information comes in the form of an [Order] or an [Announcement]
 *
 * Orders should be sent whenever it is available, and any accompanying [Announcement]s may be set either when dispatched, or through [setImperator]
 *
 * How the information is gathered is entirely up to the implementation.
 */
interface Scout {
    /**
     * Responsible for setting the [Imperator] that this scout should report to
     */
    fun setImperator(imperator: Imperator)

    fun addAnnouncements(order: Order)

    /**
     * Return the name of this [Scout] implementation
     */
    fun getName(): String
}

/**
 * An implementation of [Order] could be considered sort of like an event - [Scout] implementations may send this to an [Imperator] ***however*** it is not required.
 * It may be sent by anyone with access to an [Imperator] instance.
 *
 * [Order] implementations should only be filtered by [Watchtower]s, and only accepted by [Soldier]s who are prepared to handle this implemention.
 */
interface Order {
    fun setOrigin(scout: Scout)
    fun getOrigin(): Optional<Scout>

    fun addAnnouncement(announcement: Announcement<*>)
    fun getAnnouncements(): Collection<Announcement<*>>
    fun getAnnouncement(name: String): Optional<Announcement<*>> = Optional.ofNullable(getAnnouncements().firstOrNull { announcement -> announcement.getName() == name })

    fun getName(): String
}

/**
 * An implementation of [Announcement] is considered metadata, of sorts. It is additional information either about or pertaining to the [Order] object it is connected to.
 *
 * [Announcement]s may contain either very general information about an [Order], such as the time it was sent,
 * or they may contain very specific information related to the type of [Order] that was dispatched.
 */
interface Announcement<out T> {
    fun getInfo(): T
    fun getName(): String
}

/**
 * [Watchtower] implementations are, essentially, filters that are employed by [Soldier]s to make sure that they only listen to [Order]s that they want to.
 * Nothing too fancy here
 */
interface Watchtower {
    fun allow(order: Order): Boolean

    fun getName(): String
}

/**
 * The [Soldier] implementations are where magic begins to happen. These are the front lines - this is where [Order]s are processed, and [Announcement]s read out.
 */
interface Soldier {
    fun getWatchtowers(): Collection<Watchtower>
    fun command(order: Order)

    fun getName(): String
}

/**
 * The [Spy] implementations are responsible for basic intrigue among the ranks. Similar to [Soldier]s, they have [Watchtowers][getWatchtowers], and then [fiddle] with the [Order]s
 */
interface Spy {
    fun getWatchtowers(): Collection<Watchtower>
    fun fiddle(order: Order): Order

    fun getName(): String
}