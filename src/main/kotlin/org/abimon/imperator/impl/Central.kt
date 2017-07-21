package org.abimon.imperator.impl

import org.abimon.imperator.handle.*
import java.util.*

/**
 * Basic implementation of an [Imperator]
 */
open class BasicImperator: Imperator {
    private val spies = LinkedList<Spy>()
    val scouts = ArrayList<Scout>()
    val soldiers = ArrayList<Soldier>()

    override fun dispatch(originalOrder: Order) {
        var order = originalOrder

        spies.forEach { spy -> if(spy.getWatchtowers().all { watchtower -> watchtower.allow(order) }) order = spy.fiddle(order) }
        scouts.forEach { scout -> scout.addAnnouncements(order) }
        soldiers.filter { soldier -> soldier.getWatchtowers().all { watchtower -> watchtower.allow(order) } }.forEach { soldier -> soldier.command(order) }
    }

    override fun hireSpy(spy: Spy, priority: Int) = spies.add(priority.coerceAtLeast(0).coerceAtMost(spies.size), spy)
    override fun getSpies(): List<Spy> = spies
    override fun fireSpy(spy: Spy): Optional<Spy> {
        if(spies.remove(spy))
            return Optional.of(spy)
        return Optional.empty()
    }
    override fun fireSpyByName(spyName: String): Optional<Spy> {
        val spy = spies.firstOrNull { spy -> spy.getName() == spyName } ?: return Optional.empty()
        return fireSpy(spy)
    }
    override fun fireAllSpies() = spies.clear()

    override fun hireScout(scout: Scout) {
        scout.setImperator(this)
        scouts.add(scout)
    }
    override fun getScouts(): List<Scout> = scouts
    override fun fireScout(scout: Scout): Optional<Scout> {
        if(scouts.remove(scout))
            return Optional.of(scout)
        return Optional.empty()
    }
    override fun fireScoutByName(spyName: String): Optional<Scout> {
        val scout = scouts.firstOrNull { scout -> scout.getName() == spyName } ?: return Optional.empty()
        return fireScout(scout)
    }
    override fun fireAllScouts() = scouts.clear()

    override fun hireSoldier(soldier: Soldier) {
        soldiers.add(soldier)
    }
    override fun getSoldiers(): List<Soldier> = soldiers
    override fun fireSoldier(soldier: Soldier): Optional<Soldier> {
        if(soldiers.remove(soldier))
            return Optional.of(soldier)
        return Optional.empty()
    }
    override fun fireSoldierByName(soldierName: String): Optional<Soldier> {
        val soldier = soldiers.firstOrNull { soldier -> soldier.getName() == soldierName } ?: return Optional.empty()
        return fireSoldier(soldier)
    }
    override fun fireAllSoldiers() = soldiers.clear()
}

open class BasicSpy(private val name: String, private val watchtowers: Collection<Watchtower>, val fiddle: (Order) -> Order): Spy {
    override fun getWatchtowers(): Collection<Watchtower> = watchtowers

    override fun fiddle(order: Order): Order = fiddle.invoke(order)

    override fun getName(): String = name
}

open class BasicSoldier(private val name: String, private val watchtowers: Collection<Watchtower>, val turn: (Order) -> Unit): Soldier {

    constructor(name: String, watchtower: Watchtower, turn: (Order) -> Unit) : this(name, Collections.singleton(watchtower), turn)

    override fun getWatchtowers(): Collection<Watchtower> = watchtowers

    override fun command(order: Order) = turn.invoke(order)

    override fun getName(): String = name
}

open class BasicWatchtower(private val name: String, val regulations: (Order) -> Boolean): Watchtower {
    override fun allow(order: Order): Boolean = regulations.invoke(order)

    override fun getName(): String = name

}

abstract class BaseOrder(private val name: String = "", originScout: Scout? = null): Order {
    private var origin: Optional<Scout> = Optional.ofNullable(originScout)
    private val announcements = ArrayList<Announcement<*>>()

    override fun setOrigin(scout: Scout) {
        origin = Optional.of(scout)
    }

    override fun getOrigin(): Optional<Scout> = origin

    override fun addAnnouncement(announcement: Announcement<*>) {
        announcements.add(announcement)
    }

    override fun getAnnouncements(): Collection<Announcement<*>> = announcements

    override fun getName(): String = name
}

open class InstanceWatchtower<in T: Order>(private val t: Class<T>, private val name: String, val additionalRegulation: (T) -> Boolean = { true }): Watchtower {
    companion object {
        inline operator fun <reified T : Order> invoke(name: String = "Watching out for orders of type ${T::class.simpleName}") = InstanceWatchtower(T::class.java, name)
        inline operator fun <reified T : Order> invoke(name: String = "Watching out for orders of type ${T::class.simpleName}", noinline additionalRegulation: (T) -> Boolean) = InstanceWatchtower(T::class.java, name, additionalRegulation)
    }

    override fun getName(): String = name

    @Suppress("UNCHECKED_CAST")
    override fun allow(order: Order): Boolean = t.isInstance(order) && additionalRegulation.invoke(order as T)
}

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