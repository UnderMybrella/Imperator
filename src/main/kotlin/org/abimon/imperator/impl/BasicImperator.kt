package org.abimon.imperator.impl

import org.abimon.imperator.handle.*
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties

/**
 * Basic implementation of an [Imperator]
 */
open class BasicImperator: Imperator {
    override val spies = LinkedList<Spy>()
    override val scouts = ArrayList<Scout>()
    override val soldiers = ArrayList<Soldier>()

    override fun dispatch(originalOrder: Order): List<Soldier> {
        var order = originalOrder

        spies.forEach { spy -> if (spy.getWatchtowers().all { watchtower -> watchtower.allow(order) }) order = spy.fiddle(order) }
        scouts.forEach { scout -> scout.addAnnouncements(order) }
        val applicable = soldiers.filter { soldier -> soldier.getWatchtowers().all { watchtower -> watchtower.allow(order) } }
        applicable.forEach { soldier -> soldier.command(order) }

        return applicable
    }

    override fun hireSpy(spy: Spy, priority: Int) = spies.add(priority.coerceAtLeast(0).coerceAtMost(spies.size), spy)
    override fun fireSpy(spy: Spy): Spy? {
        if(spies.remove(spy))
            return spy
        return null
    }
    override fun fireSpyByName(spyName: String): Spy? {
        return fireSpy(spies.firstOrNull { spy -> spy.getName() == spyName } ?: return null)
    }
    override fun fireAllSpies() = spies.clear()

    override fun hireScout(scout: Scout) {
        scout.setImperator(this)
        scouts.add(scout)
    }
    override fun fireScout(scout: Scout): Scout? {
        if(scouts.remove(scout))
            return scout
        return null
    }
    override fun fireScoutByName(spyName: String): Scout? {
        return fireScout(scouts.firstOrNull { scout -> scout.getName() == spyName } ?: return null)
    }
    override fun fireAllScouts() = scouts.clear()

    override fun hireSoldier(soldier: Soldier) {
        soldiers.add(soldier)
    }
    override fun fireSoldier(soldier: Soldier): Soldier? {
        if(soldiers.remove(soldier))
            return soldier
        return null
    }
    override fun fireSoldierByName(soldierName: String): Soldier? {
        return fireSoldier(soldiers.firstOrNull { soldier -> soldier.getName() == soldierName } ?: return null)
    }
    override fun fireAllSoldiers() = soldiers.clear()

    override fun hireSoldiers(barracks: Any) {
        barracks.javaClass.kotlin.memberProperties.forEach { recruit ->
            if((recruit.returnType.classifier as? KClass<*>)?.isSubclassOf(Soldier::class) == true || recruit.returnType.classifier == Soldier::class)
                soldiers.add(recruit.get(barracks) as? Soldier ?: return@forEach)
        }
    }

    override fun fireSoldiers(barracks: Any) {
        barracks.javaClass.kotlin.memberProperties.forEach { recruit ->
            if((recruit.returnType.classifier as? KClass<*>)?.isSubclassOf(Soldier::class) == true || recruit.returnType.classifier == Soldier::class)
                soldiers.remove(recruit.get(barracks) as? Soldier ?: return@forEach)
        }
    }
}