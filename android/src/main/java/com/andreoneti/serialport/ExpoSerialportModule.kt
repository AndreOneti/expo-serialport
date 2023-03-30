package com.andreoneti.serialport

import android.content.Context
import android.content.SharedPreferences

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.hardware.usb.UsbRequest
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDeviceConnection

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap

import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.modules.Module

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

class ExpoSerialportModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("ExpoSerialport")

    Function("getUsbDevices") {
      return@Function getUsbDevices()
    }

    Function("connectToDevice") { deviceName: String ->
      return@Function connectToDevice(deviceName)
    }

    Function("comunicate") { deviceName: String ->
      return@Function comunicate(deviceName)
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
        usbDeviceMap.putInt("deviceProtocol", usbDevice.deviceProtocol)
        usbDeviceMap.putInt("interfaceCount", usbDevice.interfaceCount)
        usbDeviceMap.putString("productName", usbDevice.productName)
        usbDeviceMap.putString("serialNumber", usbDevice.serialNumber)

        usbDevicesArray.pushMap(usbDeviceMap)
      }
    }

    return usbDevicesArray
  }

  private fun connectToDevice(deviceName: String): UsbDeviceConnection? {
    val usbManager: UsbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager
    val usbDeviceList: List<UsbDevice>? = usbManager.deviceList.values.toList()

    val usbDevice: UsbDevice? = usbDeviceList?.find { it.deviceName == deviceName }

    if (usbDevice != null) {
      val usbInterface: UsbInterface? = usbDevice.getInterface(0)

      val usbDeviceConnection: UsbDeviceConnection? = usbManager.openDevice(usbDevice)

      if (usbDeviceConnection != null && usbDeviceConnection.claimInterface(usbInterface, true)) {
        return usbDeviceConnection
      } else {
        usbDeviceConnection?.close()
      }
    }

    return null
  }

  private fun comunicate(deviceName: String): String {
    // Get the USB device and open a connection
    val usbManager: UsbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager
    val usbDeviceList: List<UsbDevice>? = usbManager.deviceList.values.toList()

    val device: UsbDevice? = usbDeviceList?.find { it.deviceName == deviceName }
    val connection = usbManager.openDevice(device)

    // Find the bulk endpoints for reading and writing data
    val inEndpoint = device?.getInterface(0)?.getEndpoint(0)
    val outEndpoint = device?.getInterface(0)?.getEndpoint(1)

    // Send a control message to configure the device
    val requestType = UsbConstants.USB_TYPE_VENDOR or UsbConstants.USB_DIR_OUT
    val request = 0x01
    val value = 0
    val index = 0
    val buffer = byteArrayOf(0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08)
    val length = buffer.size
    val timeout = 1000
    val result = connection?.controlTransfer(requestType, request, value, index, buffer, length, timeout)

    // Read data from the device
    val readBuffer = ByteArray(1024)
    val readTimeout = 5000
    val readLength = connection?.bulkTransfer(inEndpoint, readBuffer, readBuffer.size, readTimeout)

    // Print the data to the console
    val data = readBuffer.copyOf(readLength ?: 0)

    // Close the connection
    connection?.close()

    return "Received data: ${data.toHexString()}"
  }
}
