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

    Function("sendUsbData") { deviceName: String, data: String ->
      return@Function sendUsbData(deviceName, data)
    }
  }

  private val context
  get() = requireNotNull(appContext.reactContext)

  private fun getPreferences(): SharedPreferences {
    return context.getSharedPreferences(context.packageName + ".settings", Context.MODE_PRIVATE)
  }

  private fun getUsbManager(): UsbManager {
    return context?.getSystemService(Context.USB_SERVICE) as UsbManager
  }

  private fun getUsbDevices(): WritableArray {
    val usbManager: UsbManager = getUsbManager()
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
    val usbManager: UsbManager = getUsbManager()
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

  /**
   * Sends a byte array of data to a USB device and reads the response.
   *
   * @param deviceName The USB deviceName to send data to.
   * @param data The byte array of data to send.
   * @return The response from the USB device as a string, or null if an error occurs.
   */
  private fun sendUsbData(deviceName: String, data: String): String? {
    val usbManager: UsbManager = getUsbManager()
    val usbDeviceList: List<UsbDevice>? = usbManager.deviceList.values.toList()

    val device: UsbDevice? = usbDeviceList?.find { it.deviceName == deviceName }
    if (device == null) {
      return null
    }

    // Open the device connection
    val connection = usbManager.openDevice(device)

    // If the connection is null, return null
    if (connection == null) {
      return null
    }

    try {
      // Claim the interface
      if (!connection.claimInterface(device.getInterface(0), true)) {
        return "Error claiming interface"
      }

      // Get the endpoint
      val endpoint = device.getInterface(0).getEndpoint(0)

      // Send the data
      val dataBytes = data.toByteArray()
      val sent = connection.bulkTransfer(endpoint, dataBytes, dataBytes.size, 5000)

      // If the data was not sent, return an error message
      if (sent < 0) {
        return "Error sending data"
      }

      // Read the response
      val buffer = ByteArray(endpoint.maxPacketSize)
      val received = connection.bulkTransfer(endpoint, buffer, buffer.size, 5000)

      // If the response was not received, return an error message
      if (received < 0) {
        return "Error receiving data"
      }

      // Return the response as a string
      return String(buffer, 0, received)
    } catch (e: Exception) {
      // If an exception is thrown, return null
      return null
    } finally {
      // Release the interface and close the connection
      connection.releaseInterface(device.getInterface(0))
      connection.close()
    }
  }
}
