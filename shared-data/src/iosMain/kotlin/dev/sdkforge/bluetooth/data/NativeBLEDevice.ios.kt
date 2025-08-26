@file:Suppress("ktlint:standard:function-signature", "ktlint:standard:class-signature")

package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLEDevice
import dev.sdkforge.bluetooth.domain.BLEDeviceState
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralStateConnected
import platform.CoreBluetooth.CBPeripheralStateConnecting
import platform.CoreBluetooth.CBPeripheralStateDisconnected
import platform.CoreBluetooth.CBPeripheralStateDisconnecting

internal actual class NativeBLEDevice(
    internal val centralManager: CBCentralManager,
    internal val peripheral: CBPeripheral,
) : BLEDevice {

    actual override val name: String?
        get() = peripheral.name

    actual override val state: BLEDeviceState
        get() = when (peripheral.state) {
            CBPeripheralStateConnected -> BLEDeviceState.CONNECTED
            CBPeripheralStateConnecting -> BLEDeviceState.CHANGING
            CBPeripheralStateDisconnected -> BLEDeviceState.CONNECTED
            CBPeripheralStateDisconnecting -> BLEDeviceState.CHANGING
            else -> BLEDeviceState.UNKNOWN
        }

    //region Connection

    actual override fun connect() {
        centralManager.connectPeripheral(peripheral, null)
    }

    actual override fun disconnect() {
        centralManager.cancelPeripheralConnection(peripheral)
    }

    //endregion

    //region Discovery

    actual override fun discoverServices() {
        peripheral.discoverServices(null)
    }

    //endregion
}
