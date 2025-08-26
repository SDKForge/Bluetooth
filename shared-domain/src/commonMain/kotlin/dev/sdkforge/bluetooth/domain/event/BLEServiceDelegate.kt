@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.domain.event

import dev.sdkforge.bluetooth.domain.BLECharacteristic

interface BLEServiceDelegate {
    fun onCharacteristicDiscovered(characteristic: BLECharacteristic) = Unit
}
