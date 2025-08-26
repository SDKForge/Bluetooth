package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLEService

internal expect class NativeBLEService : BLEService {
    override val uuid: String

    override fun discoverCharacteristics()
}
