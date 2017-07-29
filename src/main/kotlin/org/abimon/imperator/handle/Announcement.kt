package org.abimon.imperator.handle

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