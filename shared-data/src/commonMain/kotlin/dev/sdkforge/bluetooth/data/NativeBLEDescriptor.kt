@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLEDescriptor

internal expect class NativeBLEDescriptor : BLEDescriptor {
    override val uuid: String

    override fun read()
    override fun write(value: ByteArray)
}
