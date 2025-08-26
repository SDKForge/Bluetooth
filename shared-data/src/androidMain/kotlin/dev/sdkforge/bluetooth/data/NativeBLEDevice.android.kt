@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.content.Context
import androidx.annotation.RequiresPermission
import dev.sdkforge.bluetooth.domain.BLEDevice
import dev.sdkforge.bluetooth.domain.BLEDeviceState

internal actual class NativeBLEDevice(
    internal val context: Context,
    internal val device: BluetoothDevice,
    internal val gattCallback: BluetoothGattCallback,
) : BLEDevice {

    private var gatt: BluetoothGatt? = null

    actual override val name: String?
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        get() = device.name

    actual override val state: BLEDeviceState
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        get() = when (device.bondState) {
            BluetoothDevice.BOND_BONDED -> BLEDeviceState.CONNECTED
            BluetoothDevice.BOND_BONDING -> BLEDeviceState.CHANGING
            BluetoothDevice.BOND_NONE -> BLEDeviceState.UNKNOWN
            else -> BLEDeviceState.UNKNOWN
        }

    //region Connection

    @Suppress("ktlint:standard:comment-wrapping")
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    actual override fun connect() {
        gatt = device.connectGatt(
            /* context */ context,
            /* autoConnect */ false,
            /* callback */ gattCallback,
        )
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    actual override fun disconnect() {
        gatt?.disconnect()
    }

    //endregion

    //region Discovery

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    actual override fun discoverServices() {
        gatt?.discoverServices()
    }

    //endregion
}
