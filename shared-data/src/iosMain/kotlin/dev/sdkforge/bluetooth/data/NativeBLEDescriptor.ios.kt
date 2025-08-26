@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLEDescriptor
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService

internal actual class NativeBLEDescriptor(
    internal val peripheral: CBPeripheral,
    internal val service: CBService,
    internal val characteristic: CBCharacteristic,
    internal val descriptor: CBDescriptor,
) : BLEDescriptor {

    actual override val uuid: String
        get() = descriptor.UUID.UUIDString

    actual override fun read() {
        peripheral.readValueForDescriptor(descriptor)
    }

    actual override fun write(value: ByteArray) {
        peripheral.writeValue(data = value.toNSData(), forDescriptor = descriptor)
    }
}
