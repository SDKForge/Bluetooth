@file:Suppress("ktlint:standard:class-signature", "ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.content.Context
import androidx.annotation.RequiresPermission
import dev.sdkforge.bluetooth.domain.BLE
import dev.sdkforge.bluetooth.domain.BLEDevice
import dev.sdkforge.bluetooth.domain.BLEScanError
import dev.sdkforge.bluetooth.domain.BLEService
import dev.sdkforge.bluetooth.domain.event.BLEDelegate

actual class NativeBLE(
    private val context: Context,
    internal actual val bleDelegate: BLEDelegate,
) : BLE {

    private val devices: HashMap<String, BLEDevice> = hashMapOf()
    private val services: HashMap<String, BLEService> = hashMapOf()

    private val delegate: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bleScanCallback = object : ScanCallback() {
        override fun onScanResult(
            callbackType: Int,
            result: android.bluetooth.le.ScanResult?,
        ) {
            if (result != null) {
                val device = NativeBLEDevice(
                    context = context,
                    device = result.device,
                    gattCallback = bluetoothGattCallback,
                )
                devices[device.device.address] = device
                bleDelegate.onDiscovered(device)
            }
        }

        override fun onBatchScanResults(
            results: List<android.bluetooth.le.ScanResult?>?,
        ) {
            results.orEmpty().mapNotNull { it }.map { result ->
                val device = NativeBLEDevice(
                    context = context,
                    device = result.device,
                    gattCallback = bluetoothGattCallback,
                )
                devices[device.device.address] = device
                bleDelegate.onDiscovered(device)
            }
        }

        override fun onScanFailed(
            errorCode: Int,
        ) {
            bleDelegate.onError(BLEScanError(errorCode))
        }
    }

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onServiceChanged(
            gatt: BluetoothGatt,
        ) = Unit

        override fun onMtuChanged(
            gatt: BluetoothGatt?,
            mtu: Int,
            status: Int,
        ) = Unit

        override fun onReadRemoteRssi(
            gatt: BluetoothGatt?,
            rssi: Int,
            status: Int,
        ) = Unit

        override fun onReliableWriteCompleted(
            gatt: BluetoothGatt?,
            status: Int,
        ) = Unit

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int,
        ) = Unit

        override fun onDescriptorRead(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int,
            value: ByteArray,
        ) = Unit

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
        ) = Unit

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int,
        ) = Unit

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int,
        ) = Unit

        override fun onServicesDiscovered(
            gatt: BluetoothGatt?,
            status: Int,
        ) {
            if (gatt == null) return
            if (status != BluetoothGatt.GATT_SUCCESS) return

            gatt.services.orEmpty().forEach { service ->
                val service =
                    NativeBLEService(gatt = gatt, service = service, bleDelegate = bleDelegate)
                services[service.uuid] = service
                bleDelegate.onServiceDiscovered(service)
            }
        }

        override fun onConnectionStateChange(
            gatt: BluetoothGatt?,
            status: Int,
            newState: Int,
        ) {
            val device = devices[gatt?.device?.address] ?: return

            if (status != BluetoothGatt.GATT_SUCCESS) return

            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> bleDelegate.onConnected(device)
                BluetoothProfile.STATE_DISCONNECTED -> bleDelegate.onDisconnected(device)
            }
        }

        override fun onPhyRead(
            gatt: BluetoothGatt?,
            txPhy: Int,
            rxPhy: Int,
            status: Int,
        ) = Unit

        override fun onPhyUpdate(
            gatt: BluetoothGatt?,
            txPhy: Int,
            rxPhy: Int,
            status: Int,
        ) = Unit
    }

    //region Initialization & State

    actual override val isEnabled: Boolean
        get() = delegate.adapter.state == BluetoothAdapter.STATE_ON

    //endregion

    //region Scanning

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    actual override fun startScan() {
        delegate.adapter.bluetoothLeScanner.startScan(bleScanCallback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    actual override fun stopScan() {
        delegate.adapter.bluetoothLeScanner.stopScan(bleScanCallback)
    }

    //endregion
}
