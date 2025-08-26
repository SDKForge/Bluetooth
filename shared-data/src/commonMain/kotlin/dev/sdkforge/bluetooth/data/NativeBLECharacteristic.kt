@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLECharacteristic

internal expect class NativeBLECharacteristic : BLECharacteristic {
    override val uuid: String
    override val isNotifying: Boolean

    override fun discoverDescriptors()
    override fun read()
    override fun write(value: ByteArray)
}
