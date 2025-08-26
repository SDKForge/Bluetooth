package dev.sdkforge.bluetooth.domain

interface BLE {

    //region Initialization & State

    val isEnabled: Boolean

    //endregion

    //region Scanning

    fun startScan()
    fun stopScan()

    //endregion
}
