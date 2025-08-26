package dev.sdkforge.bluetooth.domain.event

abstract class BLEDelegate :
    BLEScanDelegate,
    BLEDeviceDelegate,
    BLEServiceDelegate,
    BLECharacteristicDelegate,
    BLEDescriptorDelegate
