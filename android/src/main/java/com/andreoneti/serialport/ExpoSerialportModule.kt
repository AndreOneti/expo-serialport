package com.andreoneti.serialport

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.content.SharedPreferences

import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.modules.Module

class ExpoSerialportModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("ExpoSerialport")

    Function("getTheme") {
      return@Function "system"
    }

    Function("getDeviceList") {
      return@Function getDeviceList()
    }
  }

  private val context
  get() = requireNotNull(appContext.reactContext)

  private fun getPreferences(): SharedPreferences {
    return context.getSharedPreferences(context.packageName + ".settings", Context.MODE_PRIVATE)
  }

  private fun getDeviceList(): List<UsbDevice>? {
    var usbManager: UsbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager

    val usbDeviceList: List<UsbDevice>? = usbManager.deviceList.values.toList()

    // "Device Name: ${usbDevice.deviceName},
    // Vendor ID: ${usbDevice.vendorId},
    // Product ID: ${usbDevice.productId}"

    return usbDeviceList
  }
}
