@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.os.Build
import androidx.annotation.RequiresPermission
import dev.sdkforge.bluetooth.domain.BLEDescriptor

internal actual class NativeBLEDescriptor(
    internal val gatt: BluetoothGatt,
    internal val service: BluetoothGattService,
    internal val characteristic: BluetoothGattCharacteristic,
    internal val descriptor: BluetoothGattDescriptor,
) : BLEDescriptor {

    actual override val uuid: String
        get() = descriptor.uuid.toString()

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    actual override fun read() {
        gatt.readDescriptor(descriptor)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    actual override fun write(value: ByteArray) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                gatt.writeDescriptor(descriptor, value)
            }

            else -> {
                descriptor.value = value
                gatt.writeDescriptor(descriptor)
            }
        }
    }
}
