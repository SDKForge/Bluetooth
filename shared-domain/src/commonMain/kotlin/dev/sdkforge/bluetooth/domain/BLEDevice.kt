package dev.sdkforge.bluetooth.domain

interface BLEDevice {
    val name: String?
    val state: BLEDeviceState

    //region Connection

    fun connect()
    fun disconnect()

    //endregion

    //region Discovery

    fun discoverServices()

    //endregion
}
