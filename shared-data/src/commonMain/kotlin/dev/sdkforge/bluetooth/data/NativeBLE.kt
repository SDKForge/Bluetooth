package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLE
import dev.sdkforge.bluetooth.domain.event.BLEDelegate

internal expect class NativeBLE : BLE {
    internal val bleDelegate: BLEDelegate

    override val isEnabled: Boolean

    override fun startScan()
    override fun stopScan()
}
