package model;

public enum GameFile {

    DATA_WIN("data.win"),
    DEADBOLT_GAME("deadbolt_game.exe"),
    DIA_FP("dia_fp.json");

    public final String label;

    private GameFile(String label) {
        this.label = label;
    }

}
