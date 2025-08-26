package dev.sdkforge.bluetooth.data

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattService
import dev.sdkforge.bluetooth.domain.BLECharacteristic
import dev.sdkforge.bluetooth.domain.BLEService
import dev.sdkforge.bluetooth.domain.event.BLEDelegate

internal actual class NativeBLEService(
    internal val gatt: BluetoothGatt,
    internal val service: BluetoothGattService,
    internal val bleDelegate: BLEDelegate,
) : BLEService {
    private val characteristics: HashMap<String, BLECharacteristic> = hashMapOf()

    actual override val uuid: String
        get() = service.uuid.toString()

    //region Discovery

    actual override fun discoverCharacteristics() {
        service.characteristics.orEmpty().map { characteristic ->
            val characteristic = NativeBLECharacteristic(
                gatt = gatt,
                service = service,
                characteristic = characteristic,
                bleDelegate = bleDelegate,
            )
            characteristics[characteristic.uuid] = characteristic
            bleDelegate.onCharacteristicDiscovered(characteristic)
        }
    }

    //endregion
}
