import ExpoSerialportModule from "./ExpoSerialportModule";

export function getTheme(): string {
  return ExpoSerialportModule.getTheme();
}

export function getDeviceList(): unknown[] {
  return ExpoSerialportModule.getDeviceList();
}
