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

  private fun getDeviceList(): ArrayList<UsbDevice> {
    var deviceList: ArrayList<UsbDevice> = ArrayList<UsbDevice>()

    var usbManager: UsbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager

    var devicesList: HashMap<String, UsbDevice> = usbManager.getDeviceList()

    for (usbDevide in devicesList.values) {
      deviceList.add(usbDevide)
    }
    // devicesList.forEach { (key, device) ->
    //   deviceList.add(device)
    // }

    return deviceList
  }
}
