package org.abimon.imperator.impl

import org.abimon.imperator.handle.Announcement
import org.abimon.imperator.handle.Order
import org.abimon.imperator.handle.Scout
import java.util.*

abstract class BaseOrder(override val name: String = "", override var scout: Scout? = null): Order {
    private val announcements = ArrayList<Announcement<*>>()

    override fun addAnnouncement(announcement: Announcement<*>) {
        announcements.add(announcement)
    }

    override fun getAnnouncements(): Collection<Announcement<*>> = announcements
}