@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.domain.event

import dev.sdkforge.bluetooth.domain.BLEDevice
import dev.sdkforge.bluetooth.domain.BLEScanError

interface BLEScanDelegate {
    fun onDiscovered(device: BLEDevice) = Unit
    fun onError(error: BLEScanError) = Unit
}
