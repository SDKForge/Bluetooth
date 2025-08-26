@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.domain

interface BLECharacteristic {
    val uuid: String
    val isNotifying: Boolean

    //region Discovery

    fun discoverDescriptors()

    //endregion

    fun read()
    fun write(value: ByteArray)
}
