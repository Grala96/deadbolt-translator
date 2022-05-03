package model;

public enum DataType {

    UNKNOWN_DATA, FAKE_DIALOG_GAME, DIALOG_GAME

    // We have 3 categories relative to which the HEX data fragment is recognised.
    // UNKNOWN_DATA - Data that does not match the pattern that should not be changed.
    // FAKE_DIALOG_GAME - Data that matched the pattern, but on additional verification was recognised as not being in-game dialogue.
    // DIALOG_GAME - Data matching the pattern recognised as in-game dialogue.

}
