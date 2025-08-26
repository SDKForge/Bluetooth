@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLECharacteristic
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicWriteWithoutResponse
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService

internal actual class NativeBLECharacteristic(
    internal val peripheral: CBPeripheral,
    internal val service: CBService,
    internal val characteristic: CBCharacteristic,
) : BLECharacteristic {

    actual override val uuid: String
        get() = characteristic.UUID.UUIDString

    actual override val isNotifying: Boolean
        get() = characteristic.isNotifying

    //region Discovery

    actual override fun discoverDescriptors() {
        peripheral.discoverDescriptorsForCharacteristic(characteristic)
    }

    //endregion

    actual override fun read() {
        peripheral.readValueForCharacteristic(characteristic)
    }

    actual override fun write(value: ByteArray) {
        peripheral.writeValue(data = value.toNSData(), forCharacteristic = characteristic, type = CBCharacteristicWriteWithoutResponse)
    }
}
