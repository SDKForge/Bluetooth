@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.ui

import androidx.compose.runtime.Composable
import dev.sdkforge.bluetooth.domain.BLE
import dev.sdkforge.bluetooth.domain.event.BLEDelegate

@Composable
expect fun rememberBLE(bleDelegate: BLEDelegate): BLE
