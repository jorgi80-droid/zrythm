import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

// Standalone UI prototype for the Zrythm Android port.
// This is intentionally isolated from the desktop app until the Android build
// toolchain and audio backend are validated.
ApplicationWindow {
    id: root
    visible: true
    width: 960
    height: 540
    minimumWidth: 640
    minimumHeight: 360
    title: "Zrythm Mobile — Prototype"
    color: "#101312"

    property string activePage: "Arrangement"
    property int selectedTrack: 0
    property bool playing: false
    property real zoom: 1.0

    readonly property color panel: "#191e1c"
    readonly property color panelRaised: "#222a26"
    readonly property color borderColor: "#303a34"
    readonly property color accent: "#29d989"
    readonly property color textPrimary: "#edf5ef"
    readonly property color textMuted: "#98a79d"

    function selectPage(page) { activePage = page }

    ColumnLayout {
        anchors.fill: parent
        spacing: 0

        // Compact transport bar: deliberately large touch targets.
        Rectangle {
            Layout.fillWidth: true
            Layout.preferredHeight: 62
            color: "#171c19"
            border.color: root.borderColor

            RowLayout {
                anchors.fill: parent
                anchors.leftMargin: 12
                anchors.rightMargin: 12
                spacing: 10

                Label {
                    text: "Z"
                    color: root.accent
                    font.pixelSize: 30
                    font.bold: true
                }
                ColumnLayout {
                    spacing: 0
                    Label { text: "ZRYTHM"; color: root.textPrimary; font.bold: true; font.pixelSize: 14 }
                    Label { text: "MOBILE PROTOTYPE"; color: root.textMuted; font.pixelSize: 9 }
                }
                Rectangle { Layout.preferredWidth: 1; Layout.fillHeight: true; Layout.margins: 8; color: root.borderColor }
                ColumnLayout {
                    spacing: 0
                    Label { text: "TEMPO"; color: root.textMuted; font.pixelSize: 9 }
                    Label { text: "120.00 BPM"; color: root.textPrimary; font.pixelSize: 14; font.bold: true }
                }
                ColumnLayout {
                    spacing: 0
                    Label { text: "METER"; color: root.textMuted; font.pixelSize: 9 }
                    Label { text: "4 / 4"; color: root.textPrimary; font.pixelSize: 14; font.bold: true }
                }
                Item { Layout.fillWidth: true }
                ToolButton {
                    text: root.playing ? "Ⅱ" : "▶"
                    font.pixelSize: 22
                    Layout.preferredWidth: 54
                    Layout.preferredHeight: 44
                    highlighted: true
                    onClicked: root.playing = !root.playing
                    ToolTip.visible: hovered
                    ToolTip.text: "Prototype transport only — no audio engine connected"
                }
                ToolButton {
                    text: "■"
                    font.pixelSize: 18
                    Layout.preferredWidth: 48
                    Layout.preferredHeight: 44
                    onClicked: root.playing = false
                }
                Label { text: "AUDIO ENGINE: NOT CONNECTED"; color: "#f0bd65"; font.pixelSize: 9 }
            }
        }

        RowLayout {
            Layout.fillWidth: true
            Layout.fillHeight: true
            spacing: 0

            // Navigation rail works better than a desktop menu on touch screens.
            Rectangle {
                Layout.preferredWidth: 142
                Layout.fillHeight: true
                color: "#151a17"
                border.color: root.borderColor

                ColumnLayout {
                    anchors.fill: parent
                    anchors.margins: 8
                    spacing: 6
                    Label { text: "WORKSPACE"; color: root.textMuted; font.pixelSize: 10; Layout.leftMargin: 8; Layout.topMargin: 6 }
                    Repeater {
                        model: [
                            {name:"Arrangement", glyph:"▤"},
                            {name:"Piano Roll", glyph:"♬"},
                            {name:"Mixer", glyph:"☷"},
                            {name:"Browser", glyph:"▣"},
                            {name:"Automation", glyph:"⌁"}
                        ]
                        delegate: Button {
                            required property var modelData
                            Layout.fillWidth: true
                            Layout.preferredHeight: 43
                            text: modelData.glyph + "   " + modelData.name
                            highlighted: root.activePage === modelData.name
                            onClicked: root.selectPage(modelData.name)
                            background: Rectangle {
                                radius: 7
                                color: root.activePage === modelData.name ? "#204c38" : "transparent"
                                border.color: root.activePage === modelData.name ? root.accent : "transparent"
                            }
                            contentItem: Text {
                                text: parent.text
                                color: root.activePage === modelData.name ? root.accent : root.textPrimary
                                font.pixelSize: 12
                                verticalAlignment: Text.AlignVCenter
                                leftPadding: 8
                                elide: Text.ElideRight
                            }
                        }
                    }
                    Item { Layout.fillHeight: true }
                    Label { text: "PROJECT"; color: root.textMuted; font.pixelSize: 10; Layout.leftMargin: 8 }
                    Label { text: "Untitled Project"; color: root.textPrimary; font.pixelSize: 11; Layout.leftMargin: 8; Layout.bottomMargin: 8 }
                }
            }

            ColumnLayout {
                Layout.fillWidth: true
                Layout.fillHeight: true
                spacing: 0

                RowLayout {
                    Layout.fillWidth: true
                    Layout.preferredHeight: 48
                    Layout.leftMargin: 14
                    Layout.rightMargin: 14
                    Label { text: root.activePage; color: root.textPrimary; font.pixelSize: 20; font.bold: true }
                    Label {
                        text: root.activePage === "Arrangement" ? "Song timeline" : "Interactive layout preview"
                        color: root.textMuted
                        font.pixelSize: 11
                    }
                    Item { Layout.fillWidth: true }
                    ToolButton { text: "−"; onClicked: root.zoom = Math.max(0.6, root.zoom - 0.1) }
                    Label { text: Math.round(root.zoom * 100) + "%"; color: root.textMuted; font.pixelSize: 11 }
                    ToolButton { text: "+"; onClicked: root.zoom = Math.min(1.8, root.zoom + 0.1) }
                }

                // Timeline prototype. Clips are visual placeholders, not audio/MIDI data.
                Rectangle {
                    Layout.fillWidth: true
                    Layout.fillHeight: true
                    Layout.margins: 8
                    radius: 8
                    color: "#111613"
                    border.color: root.borderColor
                    clip: true

                    Column {
                        anchors.fill: parent
                        anchors.margins: 8
                        spacing: 5

                        Row {
                            height: 24
                            spacing: 2
                            Repeater {
                                model: ["1","5","9","13","17","21","25","29","33"]
                                delegate: Rectangle {
                                    required property string modelData
                                    width: (parent.width - 16) / 9
                                    height: 24
                                    color: "#202823"
                                    radius: 3
                                    Label { anchors.centerIn: parent; text: modelData; color: root.textMuted; font.pixelSize: 10 }
                                }
                            }
                        }

                        Repeater {
                            model: [
                                {name:"DRUMS", color:"#2379c8", spans:[2,2,2,1,2]},
                                {name:"BASS", color:"#24b978", spans:[3,3,2]},
                                {name:"SYNTH", color:"#9270e8", spans:[2,3,2]},
                                {name:"GUITAR", color:"#dca34c", spans:[2,2,3]},
                                {name:"VOCALS", color:"#d95f72", spans:[3,3]},
                                {name:"FX", color:"#25b9b0", spans:[1,2,1,2]}
                            ]
                            delegate: RowLayout {
                                required property var modelData
                                Layout.fillWidth: true
                                height: Math.max(34, (parent.height - 68) / 6)
                                spacing: 5
                                Rectangle {
                                    Layout.preferredWidth: 62
                                    Layout.fillHeight: true
                                    color: root.panelRaised
                                    radius: 4
                                    Label { anchors.centerIn: parent; text: modelData.name; color: root.textPrimary; font.pixelSize: 9; font.bold: true }
                                }
                                RowLayout {
                                    Layout.fillWidth: true
                                    Layout.fillHeight: true
                                    spacing: 3
                                    Repeater {
                                        model: modelData.spans
                                        delegate: Rectangle {
                                            required property int modelData
                                            Layout.fillWidth: true
                                            Layout.fillHeight: true
                                            Layout.minimumWidth: 12
                                            Layout.preferredWidth: modelData * 45 * root.zoom
                                            radius: 4
                                            color: Qt.darker(modelData ? parent.parent.parent.modelData.color : "#2379c8", 1.15)
                                            border.color: parent.parent.parent.modelData.color
                                            Label {
                                                anchors.left: parent.left
                                                anchors.leftMargin: 6
                                                anchors.verticalCenter: parent.verticalCenter
                                                text: "▰  " + parent.parent.parent.modelData.name
                                                color: "#ffffff"
                                                font.pixelSize: 9
                                                visible: parent.width > 68
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Item { Layout.fillHeight: true }
                    }
                }

                // Touch-friendly editing tools.
                Rectangle {
                    Layout.fillWidth: true
                    Layout.preferredHeight: 54
                    color: "#171c19"
                    border.color: root.borderColor
                    RowLayout {
                        anchors.fill: parent
                        anchors.leftMargin: 10
                        anchors.rightMargin: 10
                        spacing: 8
                        Repeater {
                            model: ["Select", "Draw", "Split", "Erase", "Snap 1/16"]
                            delegate: Button {
                                required property string modelData
                                text: modelData
                                Layout.fillWidth: true
                                Layout.preferredHeight: 40
                                onClicked: toolStatus.text = modelData + " tool selected (UI prototype)"
                            }
                        }
                        Label { id: toolStatus; text: "Ready"; color: root.textMuted; font.pixelSize: 10 }
                    }
                }
            }

            // Track inspector, kept narrow so the arrangement remains dominant.
            Rectangle {
                Layout.preferredWidth: 190
                Layout.fillHeight: true
                color: "#171c19"
                border.color: root.borderColor
                visible: root.width >= 820

                ColumnLayout {
                    anchors.fill: parent
                    anchors.margins: 12
                    spacing: 10
                    Label { text: "INSPECTOR"; color: root.textMuted; font.pixelSize: 10 }
                    Label { text: "Track controls"; color: root.textPrimary; font.pixelSize: 16; font.bold: true }
                    Label { text: "Volume"; color: root.textMuted; font.pixelSize: 11 }
                    Slider { Layout.fillWidth: true; from: 0; to: 1; value: 0.78 }
                    Label { text: "Pan"; color: root.textMuted; font.pixelSize: 11 }
                    Slider { Layout.fillWidth: true; from: -1; to: 1; value: 0 }
                    RowLayout {
                        Layout.fillWidth: true
                        Button { text: "M"; Layout.fillWidth: true }
                        Button { text: "S"; Layout.fillWidth: true }
                    }
                    Rectangle { Layout.fillWidth: true; height: 1; color: root.borderColor }
                    Label { text: "INSERTS"; color: root.textMuted; font.pixelSize: 10 }
                    Repeater {
                        model: ["EQ", "Compressor", "Reverb"]
                        delegate: Rectangle {
                            required property string modelData
                            Layout.fillWidth: true
                            height: 34
                            radius: 5
                            color: root.panelRaised
                            RowLayout {
                                anchors.fill: parent
                                anchors.leftMargin: 8
                                Label { text: "●"; color: root.accent; font.pixelSize: 10 }
                                Label { text: parent.parent.modelData; color: root.textPrimary; font.pixelSize: 11 }
                            }
                        }
                    }
                    Item { Layout.fillHeight: true }
                    Label {
                        text: "Mock controls only. No audio engine or plugin processing is connected."
                        color: "#f0bd65"
                        wrapMode: Text.WordWrap
                        Layout.fillWidth: true
                        font.pixelSize: 10
                    }
                }
            }
        }
    }
}
