import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoSerialportViewProps } from './ExpoSerialport.types';

const NativeView: React.ComponentType<ExpoSerialportViewProps> =
  requireNativeViewManager('ExpoSerialport');

export default function ExpoSerialportView(props: ExpoSerialportViewProps) {
  return <NativeView {...props} />;
}
