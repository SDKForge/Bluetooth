@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.sdkforge.bluetooth.data.NativeBLE
import dev.sdkforge.bluetooth.domain.BLE
import dev.sdkforge.bluetooth.domain.event.BLEDelegate

@Composable
actual fun rememberBLE(bleDelegate: BLEDelegate): BLE {
    val context = LocalContext.current

    return remember(bleDelegate, context) {
        NativeBLE(
            context = context,
            bleDelegate = bleDelegate,
        )
    }
}
