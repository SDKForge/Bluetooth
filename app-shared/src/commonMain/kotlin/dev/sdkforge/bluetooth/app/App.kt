@file:Suppress("ktlint:standard:function-signature")

package dev.sdkforge.bluetooth.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.sdkforge.bluetooth.domain.BLECharacteristic
import dev.sdkforge.bluetooth.domain.BLEDescriptor
import dev.sdkforge.bluetooth.domain.BLEDevice
import dev.sdkforge.bluetooth.domain.BLEScanError
import dev.sdkforge.bluetooth.domain.BLEService
import dev.sdkforge.bluetooth.domain.event.BLEDelegate
import dev.sdkforge.bluetooth.ui.rememberBLE

@Composable
fun App(
    modifier: Modifier = Modifier,
) = ApplicationTheme {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        val devices = remember { mutableStateListOf<BLEDevice>() }

        var isScanning by remember { mutableStateOf(false) }
        var selectedDevice by remember { mutableStateOf<BLEDevice?>(null) }

        val bleDeviceManager = rememberBLE(
            bleDelegate = object : BLEDelegate() {
                override fun onDiscovered(device: BLEDevice) {
                    devices += device
                }

                override fun onError(error: BLEScanError) = Unit
                override fun onConnected(device: BLEDevice) = Unit
                override fun onDisconnected(device: BLEDevice) = Unit
                override fun onServiceDiscovered(service: BLEService) = Unit
                override fun onCharacteristicDiscovered(characteristic: BLECharacteristic) = Unit
                override fun onDescriptorDiscovered(descriptor: BLEDescriptor) = Unit
            },
        )

        DisposableEffect(bleDeviceManager) {
            onDispose { bleDeviceManager.stopScan() }
        }

        LaunchedEffect(Unit) {
            if (bleDeviceManager.isEnabled) {
                bleDeviceManager.startScan()
                isScanning = true
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (selectedDevice == null) {
                DeviceList(devices)
                return@Box
            }
        }
    }
}

@Composable
private fun DeviceList(
    devices: List<BLEDevice>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        items(
            items = devices,
        ) { device ->
            Text(text = device.toString())
        }
    }
}
