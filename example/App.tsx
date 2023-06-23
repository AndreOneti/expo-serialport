import "expo-dev-client";

import ExpoSerialport from "expo-serialport";
import { StyleSheet, Text, TouchableOpacity, View } from "react-native";

export default function App() {
  const asyncFunction = async () => {
    const [device] = ExpoSerialport.listDevices();
    console.log({ device });
    if (!device) return;
    try {
      const serialNumber = await ExpoSerialport.getSerialNumberAsync(
        device.deviceId
      );
      console.log({
        serialNumber,
        ...device,
      });
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={() => asyncFunction()}>
        <Text> Async? </Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
