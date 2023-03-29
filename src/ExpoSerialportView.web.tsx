import * as React from 'react';

import { ExpoSerialportViewProps } from './ExpoSerialport.types';

export default function ExpoSerialportView(props: ExpoSerialportViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
