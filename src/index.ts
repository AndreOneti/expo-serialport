import ExpoSerialportModule from "./ExpoSerialportModule";

export function getTheme(): string {
  return ExpoSerialportModule.getTheme();
}

export function getUsbDevices(): unknown[] {
  return ExpoSerialportModule.getUsbDevices();
}

export default {
  getTheme,
  getUsbDevices,
};
