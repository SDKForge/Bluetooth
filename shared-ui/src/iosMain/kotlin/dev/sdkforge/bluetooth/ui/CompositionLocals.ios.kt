@file:Suppress("ktlint:standard:function-signature", "ktlint:standard:function-expression-body")

package dev.sdkforge.bluetooth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.sdkforge.bluetooth.data.NativeBLE
import dev.sdkforge.bluetooth.domain.BLE
import dev.sdkforge.bluetooth.domain.event.BLEDelegate

@Composable
actual fun rememberBLE(bleDelegate: BLEDelegate): BLE {
    return remember(bleDelegate) { NativeBLE(bleDelegate = bleDelegate) }
}
