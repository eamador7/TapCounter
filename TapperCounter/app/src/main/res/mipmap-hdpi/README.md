# App Launcher Icons

This directory and its siblings (`mipmap-mdpi`, `mipmap-xhdpi`, etc.) are intended for the application's launcher icons.

To give the app a proper icon, you need to generate a set of adaptive icons and place them in the appropriate directories.

## Recommended Steps:

1.  **Use Android Studio's Image Asset Studio:** The easiest way to create a full set of adaptive launcher icons is to use the built-in tool in Android Studio (`Right-click res folder > New > Image Asset`).
2.  **Design a Foreground and Background:** Adaptive icons consist of two layers: a foreground (e.g., your logo) and a background (e.g., a solid color or simple shape). The system will animate these layers.
3.  **Generate Icons:** The tool will automatically generate all the necessary `.png` and `.xml` files and place them in the correct `mipmap-` folders, overwriting any placeholders.
4.  **Reference the Icon:** Ensure your `AndroidManifest.xml` points to the new icon, typically `@mipmap/ic_launcher`.

This process ensures your app icon looks great on all Android devices and versions.
