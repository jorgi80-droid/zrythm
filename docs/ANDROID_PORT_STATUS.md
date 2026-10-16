# Android port status

## Goal

Investigate a real Android port of Zrythm while keeping the upstream source separate from Android-specific work.

## Current status

- [x] Create an isolated branch: `android-port-prototype`
- [x] Add a repeatable static portability audit workflow
- [ ] Run the audit and inspect its artifact
- [ ] Map Qt/QML and JUCE Android support to the current Zrythm source
- [ ] Identify desktop-only dependencies and select Android-compatible replacements
- [ ] Implement an Android application entry point and audio/MIDI backend
- [ ] Build a debug APK in CI
- [ ] Install and test on a physical Android device

## Important

The initial workflow is an inventory tool only. It does not claim that the upstream project currently builds for Android, and it does not produce an APK. An Android package is only a valid milestone after a real Android-target build succeeds and the resulting app is tested on a device.

## Build strategy

Use GitHub Actions as a remote build runner so development can be managed from a mobile browser without Termux. Keep changes on the `android-port-prototype` branch until the port is validated.
