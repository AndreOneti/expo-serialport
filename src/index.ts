import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoSerialport.web.ts
// and on native platforms to ExpoSerialport.ts
import ExpoSerialportModule from './ExpoSerialportModule';
import ExpoSerialportView from './ExpoSerialportView';
import { ChangeEventPayload, ExpoSerialportViewProps } from './ExpoSerialport.types';

// Get the native constant value.
export const PI = ExpoSerialportModule.PI;

export function hello(): string {
  return ExpoSerialportModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoSerialportModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoSerialportModule ?? NativeModulesProxy.ExpoSerialport);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoSerialportView, ExpoSerialportViewProps, ChangeEventPayload };
