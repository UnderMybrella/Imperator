package org.abimon.imperator.handle

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