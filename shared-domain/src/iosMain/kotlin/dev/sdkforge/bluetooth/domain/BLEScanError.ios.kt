package dev.sdkforge.bluetooth.domain

import platform.Foundation.NSError

actual data class BLEScanError(val error: NSError)
