import ExpoModulesCore

public class ExpoSerialportModule: Module {
  public func definition() -> ModuleDefinition {
    Name("ExpoSettings")

    Function("getTheme") { () -> String in
      "system"
    }
  }
}
