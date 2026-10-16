# Zrythm Android port prototype

This folder contains an isolated Qt Quick/QML UI prototype for the landscape-first Android experience.

## Current scope

- Landscape arrangement layout with track/clip placeholders
- Touch-sized transport and editing controls
- Workspace navigation for Arrangement, Piano Roll, Mixer, Browser, and Automation
- Track inspector controls
- Responsive hiding of the inspector on narrower windows

## Explicit limitations

This is **not yet the Zrythm application port**. The play button only changes UI state. Clips, mixer controls, and effects are placeholders and do not connect to Zrythm's DSP, session model, MIDI engine, or plugin host. This prototype has not been compiled or tested on Android.

## Next engineering milestones

1. Validate the current Zrythm revision's Qt/JUCE and third-party dependency support for Android.
2. Create an Android Qt/CMake application target and configure Android SDK/NDK/JDK.
3. Wire the UI to Zrythm's session/actions and an Android-compatible audio/MIDI backend.
4. Build a debug APK in CI, install on a physical device, and measure audio stability/latency.
5. Add actual touch editing, piano roll, mixer, project persistence, and plugin support in stages.

Do not present the prototype as a functional DAW until those integration and device tests pass.
