@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.domain

interface BLEDescriptor {
    val uuid: String

    fun read()
    fun write(value: ByteArray)
}
