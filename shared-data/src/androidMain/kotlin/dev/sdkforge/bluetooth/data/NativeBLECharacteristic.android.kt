@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
import android.bluetooth.BluetoothGattService
import android.os.Build
import androidx.annotation.RequiresPermission
import dev.sdkforge.bluetooth.domain.BLECharacteristic
import dev.sdkforge.bluetooth.domain.BLEDescriptor
import dev.sdkforge.bluetooth.domain.event.BLEDelegate

internal actual class NativeBLECharacteristic(
    internal val gatt: BluetoothGatt,
    internal val service: BluetoothGattService,
    internal val characteristic: BluetoothGattCharacteristic,
    internal val bleDelegate: BLEDelegate,
) : BLECharacteristic {

    private val descriptors: HashMap<String, BLEDescriptor> = hashMapOf()

    actual override val uuid: String
        get() = characteristic.uuid.toString()

    actual override val isNotifying: Boolean
        get() = (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0 // TODO: check if this is correct

    //region Discovery

    actual override fun discoverDescriptors() {
        characteristic.descriptors.orEmpty().map { descriptor ->
            val descriptor = NativeBLEDescriptor(
                gatt = gatt,
                service = service,
                characteristic = characteristic,
                descriptor = descriptor,
            )
            descriptors[descriptor.uuid] = descriptor
            bleDelegate.onDescriptorDiscovered(descriptor)
        }
    }

    //endregion

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    actual override fun read() {
        gatt.readCharacteristic(characteristic)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    actual override fun write(value: ByteArray) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                gatt.writeCharacteristic(characteristic, value, WRITE_TYPE_DEFAULT)
            }

            else -> {
                characteristic.value = value
                characteristic.writeType = WRITE_TYPE_DEFAULT
                gatt.writeCharacteristic(characteristic)
            }
        }
    }
}
