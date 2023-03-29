import { StyleSheet, Text, View } from 'react-native';

import * as ExpoSerialport from 'expo-serialport';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoSerialport.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
