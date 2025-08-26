@file:Suppress("ktlint:standard:class-signature", "ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLEService
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService

internal actual class NativeBLEService(
    internal val peripheral: CBPeripheral,
    internal val service: CBService,
) : BLEService {

    actual override val uuid: String
        get() = service.UUID.UUIDString

    actual override fun discoverCharacteristics() {
        peripheral.discoverCharacteristics(characteristicUUIDs = null, forService = service)
    }
}
