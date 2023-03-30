import ExpoSerialportModule from "./ExpoSerialportModule";

interface UsbDevice {
  version: string;
  vendorId: number;
  productId: number;
  deviceName: string;
  productName: string;
  deviceClass: number;
  serialNumber: string;
  deviceProtocol: number;
  deviceSubClass: number;
  interfaceCount: number;
  manufactureName: string;
}

export function getUsbDevices(): UsbDevice[] {
  return ExpoSerialportModule.getUsbDevices();
}

export function connectToDevice(deviceName: string): void {
  return ExpoSerialportModule.connectToDevice(deviceName);
}

export default {
  getUsbDevices,
};
