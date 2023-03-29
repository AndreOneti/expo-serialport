import * as ExpoSerialport from "expo-serialport";
import { StyleSheet, Text, View } from "react-native";

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoSerialport.getTheme()}</Text>
      <Text>{JSON.stringify(ExpoSerialport.getDeviceList(), null, 2)}</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
