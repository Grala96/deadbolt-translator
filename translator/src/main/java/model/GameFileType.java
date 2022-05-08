package model;

public enum GameFileType {

    DATA_WIN("data.win"),
    DEADBOLT_GAME("deadbolt_game.exe"),
    DIA_FP("dia_fp.json"),
    DIA_LV1_4("dia_lv1_4.json"),
    DIA_LV3_5("dia_lv3_5.json"),
    DIA_LV2_1("dia_lv2_1.json"),
    DIA_LV3_7("dia_lv3_7.json"),
    DIA_LV2_3("dia_lv2_3.json"),
    DIA_LV4_2("dia_lv4_2.json");

    public final String label;

    private GameFileType(String label) {
        this.label = label;
    }

}
