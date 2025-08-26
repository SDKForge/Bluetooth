@file:Suppress("ktlint:standard:class-signature", "ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.data

import dev.sdkforge.bluetooth.domain.BLE
import dev.sdkforge.bluetooth.domain.BLEScanError
import dev.sdkforge.bluetooth.domain.event.BLEDelegate
import kotlinx.cinterop.ObjCSignatureOverride
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBConnectionEvent
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBL2CAPChannel
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBService
import platform.CoreFoundation.CFAbsoluteTime
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

actual class NativeBLE(
    internal actual val bleDelegate: BLEDelegate,
) : BLE {

    private val delegate = object :
        NSObject(),
        CBCentralManagerDelegateProtocol,
        CBPeripheralDelegateProtocol {

        //region CBCentralManagerDelegateProtocol

        @ObjCSignatureOverride
        override fun centralManager(
            central: CBCentralManager,
            didConnectPeripheral: CBPeripheral,
        ) {
            bleDelegate.onConnected(
                device = NativeBLEDevice(
                    centralManager = centralManager,
                    peripheral = didConnectPeripheral,
                ),
            )
        }

        override fun centralManager(
            central: CBCentralManager,
            willRestoreState: Map<Any?, *>,
        ) = Unit

        @ObjCSignatureOverride
        override fun centralManager(
            central: CBCentralManager,
            didUpdateANCSAuthorizationForPeripheral: CBPeripheral,
        ) = Unit

        override fun centralManager(
            central: CBCentralManager,
            didDisconnectPeripheral: CBPeripheral,
            timestamp: CFAbsoluteTime,
            isReconnecting: Boolean,
            error: NSError?,
        ) {
            bleDelegate.onDisconnected(
                NativeBLEDevice(
                    centralManager = centralManager,
                    peripheral = didDisconnectPeripheral,
                ),
            )

            if (error != null) {
                bleDelegate.onError(BLEScanError(error))
            }
        }

        @ObjCSignatureOverride
        override fun centralManager(
            central: CBCentralManager,
            didDisconnectPeripheral: CBPeripheral,
            error: NSError?,
        ) {
            bleDelegate.onDisconnected(
                NativeBLEDevice(
                    centralManager = centralManager,
                    peripheral = didDisconnectPeripheral,
                ),
            )

            if (error != null) {
                bleDelegate.onError(BLEScanError(error))
            }
        }

        @ObjCSignatureOverride
        override fun centralManager(
            central: CBCentralManager,
            didFailToConnectPeripheral: CBPeripheral,
            error: NSError?,
        ) {
            if (error != null) {
                bleDelegate.onError(BLEScanError(error))
            }
        }

        override fun centralManager(
            central: CBCentralManager,
            connectionEventDidOccur: CBConnectionEvent,
            forPeripheral: CBPeripheral,
        ) = Unit

        override fun centralManager(
            central: CBCentralManager,
            didDiscoverPeripheral: CBPeripheral,
            advertisementData: Map<Any?, *>,
            RSSI: NSNumber,
        ) {
            bleDelegate.onDiscovered(
                NativeBLEDevice(
                    centralManager = centralManager,
                    peripheral = didDiscoverPeripheral,
                ),
            )
        }

        override fun centralManagerDidUpdateState(
            central: CBCentralManager,
        ) {
            /*
            if (centralManager.state == CBCentralManagerStatePoweredOn) {
                centralManager.scanForPeripheralsWithServices(
                    serviceUUIDs = null,
                    options = null,
                )
            }
             */
        }

        //endregion

        //region CBPeripheralDelegateProtocol

        @ObjCSignatureOverride
        override fun peripheral(
            peripheral: CBPeripheral,
            didUpdateNotificationStateForCharacteristic: CBCharacteristic,
            error: NSError?,
        ) = Unit

        override fun peripheral(
            peripheral: CBPeripheral,
            didDiscoverServices: NSError?,
        ) {
            peripheral.services.orEmpty().forEach { service ->
                if (service is CBService) {
                    bleDelegate.onServiceDiscovered(
                        service = NativeBLEService(
                            peripheral = peripheral,
                            service = service,
                        ),
                    )
                }
            }
        }

        @ObjCSignatureOverride
        override fun peripheral(
            peripheral: CBPeripheral,
            didWriteValueForCharacteristic: CBCharacteristic,
            error: NSError?,
        ) = Unit

        @ObjCSignatureOverride
        override fun peripheral(
            peripheral: CBPeripheral,
            didDiscoverIncludedServicesForService: CBService,
            error: NSError?,
        ) = Unit

        @ObjCSignatureOverride
        override fun peripheral(
            peripheral: CBPeripheral,
            didUpdateValueForCharacteristic: CBCharacteristic,
            error: NSError?,
        ) = Unit

        override fun peripheral(
            peripheral: CBPeripheral,
            didReadRSSI: NSNumber,
            error: NSError?,
        ) = Unit

        @ObjCSignatureOverride
        override fun peripheral(
            peripheral: CBPeripheral,
            didUpdateValueForDescriptor: CBDescriptor,
            error: NSError?,
        ) = Unit

        @ObjCSignatureOverride
        override fun peripheral(
            peripheral: CBPeripheral,
            didDiscoverCharacteristicsForService: CBService,
            error: NSError?,
        ) {
            didDiscoverCharacteristicsForService.characteristics.orEmpty()
                .forEach { characteristic ->
                    if (characteristic is CBCharacteristic) {
                        bleDelegate.onCharacteristicDiscovered(
                            NativeBLECharacteristic(
                                peripheral = peripheral,
                                service = didDiscoverCharacteristicsForService,
                                characteristic = characteristic,
                            ),
                        )
                    }
                }
        }

        @ObjCSignatureOverride
        override fun peripheral(
            peripheral: CBPeripheral,
            didDiscoverDescriptorsForCharacteristic: CBCharacteristic,
            error: NSError?,
        ) {
            didDiscoverDescriptorsForCharacteristic.descriptors.orEmpty().forEach { descriptor ->
                if (descriptor is CBDescriptor) {
                    bleDelegate.onDescriptorDiscovered(
                        NativeBLEDescriptor(
                            peripheral = peripheral,
                            service = didDiscoverDescriptorsForCharacteristic.service!!,
                            characteristic = didDiscoverDescriptorsForCharacteristic,
                            descriptor = descriptor,
                        ),
                    )
                }
            }
        }

        override fun peripheral(
            peripheral: CBPeripheral,
            didOpenL2CAPChannel: CBL2CAPChannel?,
            error: NSError?,
        ) = Unit

        @ObjCSignatureOverride
        override fun peripheral(
            peripheral: CBPeripheral,
            didWriteValueForDescriptor: CBDescriptor,
            error: NSError?,
        ) = Unit

        override fun peripheral(
            peripheral: CBPeripheral,
            didModifyServices: List<*>,
        ) = Unit

        override fun peripheralDidUpdateName(
            peripheral: CBPeripheral,
        ) = Unit

        override fun peripheralDidUpdateRSSI(
            peripheral: CBPeripheral,
            error: NSError?,
        ) = Unit

        override fun peripheralIsReadyToSendWriteWithoutResponse(
            peripheral: CBPeripheral,
        ) = Unit

        //endregion
    }

    private val centralManager: CBCentralManager = CBCentralManager(delegate, null)

    //region Initialization & State

    actual override val isEnabled: Boolean
        get() = centralManager.state == CBManagerStatePoweredOn

    //endregion

    //region Scanning

    actual override fun startScan() {
        centralManager.scanForPeripheralsWithServices(
            serviceUUIDs = null,
            options = null,
        )
    }

    actual override fun stopScan() {
        centralManager.stopScan()
    }

    //endregion
}
