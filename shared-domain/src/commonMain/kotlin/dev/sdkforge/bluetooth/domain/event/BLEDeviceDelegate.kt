@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.domain.event

import dev.sdkforge.bluetooth.domain.BLEDevice
import dev.sdkforge.bluetooth.domain.BLEService

interface BLEDeviceDelegate {
    fun onConnected(device: BLEDevice) = Unit
    fun onDisconnected(device: BLEDevice) = Unit
    fun onServiceDiscovered(service: BLEService) = Unit
}
