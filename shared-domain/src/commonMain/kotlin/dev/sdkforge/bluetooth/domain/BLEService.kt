package dev.sdkforge.bluetooth.domain

interface BLEService {
    val uuid: String

    //region Discovery

    fun discoverCharacteristics()

    //endregion
}
