package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLEDevice
import dev.sdkforge.bluetooth.domain.BLEDeviceState

internal expect class NativeBLEDevice : BLEDevice {
    override val name: String?
    override val state: BLEDeviceState

    override fun connect()
    override fun disconnect()
    override fun discoverServices()
}
