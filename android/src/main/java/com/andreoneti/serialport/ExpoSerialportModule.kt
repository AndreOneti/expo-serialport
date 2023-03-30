package com.andreoneti.serialport

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.content.SharedPreferences

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap

import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.modules.Module

class ExpoSerialportModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("ExpoSerialport")

    Function("getUsbDevices") {
      return@Function getUsbDevices()
    }
  }

  private val context
  get() = requireNotNull(appContext.reactContext)

  private fun getPreferences(): SharedPreferences {
    return context.getSharedPreferences(context.packageName + ".settings", Context.MODE_PRIVATE)
  }

  private fun getUsbDevices(): WritableArray {
    val usbManager: UsbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager
    val usbDeviceList: List<UsbDevice>? = usbManager.deviceList.values.toList()

    val usbDevicesArray: WritableArray = WritableNativeArray()

    if (usbDeviceList != null) {
      for (usbDevice in usbDeviceList) {
        val usbDeviceMap: WritableMap = WritableNativeMap()

        usbDeviceMap.putString("deviceName", usbDevice.deviceName)
        usbDeviceMap.putInt("vendorId", usbDevice.vendorId)
        usbDeviceMap.putInt("productId", usbDevice.productId)
        usbDeviceMap.putInt("deviceClass", usbDevice.deviceClass)
        usbDeviceMap.putInt("deviceSubClass", usbDevice.deviceSubClass)
        usbDeviceMap.putInt("deviceProtocol", usbDevice.deviceProtocol)
        usbDeviceMap.putInt("interfaceCount", usbDevice.interfaceCount)
        usbDeviceMap.putString("manufactureName", usbDevice.manufactureName)
        usbDeviceMap.putString("productName", usbDevice.productName)
        usbDeviceMap.putString("serialNumber", usbDevice.serialNumber)

        usbDevicesArray.pushMap(usbDeviceMap)
      }
    }

    return usbDevicesArray
  }
}
