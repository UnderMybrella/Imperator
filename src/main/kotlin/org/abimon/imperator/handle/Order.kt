package org.abimon.imperator.handle

/**
 * An implementation of [Order] could be considered sort of like an event - [Scout] implementations may send this to an [Imperator] ***however*** it is not required.
 * It may be sent by anyone with access to an [Imperator] instance.
 *
 * [Order] implementations should only be filtered by [Watchtower]s, and only accepted by [Soldier]s who are prepared to handle this implemention.
 */
interface Order {
    val name: String
    val scout: Scout?

    fun addAnnouncement(announcement: Announcement<*>)
    fun getAnnouncements(): Collection<Announcement<*>>
    fun getAnnouncement(name: String): Announcement<*>? = getAnnouncements().firstOrNull { announcement -> announcement.getName() == name }
}