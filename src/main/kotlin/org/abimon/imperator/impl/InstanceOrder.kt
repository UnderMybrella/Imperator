package org.abimon.imperator.impl

import org.abimon.imperator.handle.Scout

open class InstanceOrder<out T>(override val name: String, override var scout: Scout?, val data: T): BaseOrder(name, scout)