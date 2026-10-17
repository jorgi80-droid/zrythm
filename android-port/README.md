# LMMS Android ARM64 port experiment

This branch adds a GitHub Actions build attempt for the existing [jsmtux LMMS Qt 6/QML Android prototype](https://github.com/jsmtux/lmms/tree/qt_6_qml_android_poc). It does not replace the Zrythm source tree with LMMS source; the workflow checks out the prototype into a separate build directory.

## Build

Open **Actions → LMMS Android ARM64 → Run workflow**. If the build succeeds, download the `LMMS-Android-ARM64` artifact from the completed run.

## Current status and limitations

This is an experimental port/build pipeline, not an official LMMS Android release. The prototype author reported missing sample loading in AudioFileProcessor and other incomplete UI behavior. The CI may need dependency/build fixes before it produces an APK; a failed build is not an APK and should not be treated as a completed port.
