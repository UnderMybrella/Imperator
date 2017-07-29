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