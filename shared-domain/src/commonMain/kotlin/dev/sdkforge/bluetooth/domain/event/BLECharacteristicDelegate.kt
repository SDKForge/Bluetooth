@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.domain.event

import dev.sdkforge.bluetooth.domain.BLEDescriptor

interface BLECharacteristicDelegate {
    fun onDescriptorDiscovered(descriptor: BLEDescriptor) = Unit
}
