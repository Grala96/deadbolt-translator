package processor;

import model.PartData;
import utils.CheckSum;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static model.DataType.*;

public class HexProcessor implements DataProcessor {

    public static final int HEADER_OFFSET = 5;

    @Override
    public ArrayList<PartData> processData(ArrayList<Character> characterArrayList, String source) {

        ArrayList<PartData> structuredData = new ArrayList<>();

        PartData currentPartData = new PartData();
        ArrayList<Character> tempList = new ArrayList<>();

        // Sprawdz kazdy bajt czy przynalezy do patternu
        // Iteruj po wszystkich bajtach jeden po drugim
        for (int i = 0; i < characterArrayList.size(); i++) {

            // Sprawdz bajt (i nastepne) czy przynalezy do patternu
            if (checkByte(i, characterArrayList)) {

                if (!tempList.isEmpty()) {
                    // Jesli przynalezy to:
                    // - tymczasowo trzymane dane (uzbierane wczesniej) wrzuc do currentPartData
                    // - warunek => tempList nie jest pusty
                    // Odrzucamy w ten sposob smieci nagromadzone wczesniej, ale dla spojnosci je rowniez przechowujemy
                    currentPartData.setData(tempList);
                    currentPartData.setType(UNKNOWN_DATA); // - oznacz je jako nierozpoznane (zgromadzilismy je wczesniej)
                    currentPartData.setSourceFile(source); // dopisz zrodlo danych
                    structuredData.add(currentPartData); // - wrzuc calosc do finalnej struktury
                }

                // - wyczysc currentPartData, wyczysc tempList
                currentPartData = new PartData();

                // - zapakuj szukane dane do currentPartData (subList zgodnie z iloscia zadeklarowana w header)
                int lengthOfRecognitionGameDialog = characterArrayList.get(i + 1);
                List<Character> recognizedGameDialog = characterArrayList.subList(i, i + HEADER_OFFSET + lengthOfRecognitionGameDialog);
                currentPartData.setData(new ArrayList<>(recognizedGameDialog));

                // - oznacz je jako znany game dialog
                if (checkCharacterArrayIsFakeDialogGame(currentPartData.getData())) {
                    currentPartData.setType(FAKE_DIALOG_GAME);
                } else {
                    currentPartData.setType(DIALOG_GAME);
                }

                // Dopisujemy informacje o pliku zrodlowym
                currentPartData.setSourceFile(source);

                // - wrzuc do final structure
                structuredData.add(currentPartData);

                // - przesun index odpowiednio na nastepne dane
                // - przesuwamy + 5 - taka dlugosc ma header (HEADER_OFFSET)
                // - przesuwamy + dlugosc game dialog
                // - przesuwamy - 1 - ze wzgledu na inkrementacje w petli
                i += HEADER_OFFSET + lengthOfRecognitionGameDialog - 1;

                // - wyczysc currentPartData, wyczysc tempList
                currentPartData = new PartData();
                tempList = new ArrayList<>();

            } else {
                // wrzuc znak do tempList
                tempList.add(characterArrayList.get(i));
            }

            // jesli to ostatnia iteracja:
            // - tymczasowo trzymane dane wrzuc do currentPartData
            // - oznacz je jako nierozpoznane
            // - wrzuc calosc do finalnej struktury

            if (i == characterArrayList.size() - 1) {
                currentPartData.setData(tempList);
                currentPartData.setType(UNKNOWN_DATA);
                structuredData.add(currentPartData);
            }

        }

        // przeedytuj wszystkie obiekty nadajac im id zgodnie z kolejnoscia w tablicy
        // dodatkowo policz hash dla ciagu bajtow
        for (int i = 0; i < structuredData.size(); i++) {
            structuredData.get(i).setPartId(i);
            byte[] dataBytes = structuredData.get(i).getData().stream().map(Objects::toString).collect(Collectors.joining()).getBytes(StandardCharsets.UTF_8);
            String hash = CheckSum.sha256(dataBytes);
            structuredData.get(i).setDataChecksum(hash);
        }

        return structuredData;
    }

    public static ArrayList<Character> getFilePart(Integer position, ArrayList<Character> arrayList) {
        ArrayList<Character> result = new ArrayList<>();
        for (Integer pos = position; pos < position + 5 && pos < arrayList.size(); pos++) {
            result.add(arrayList.get(pos));
        }
        return result;
    }

    public static boolean checkByte(Integer position, ArrayList<Character> array) {
        // Szukamy rozpoczęcia dla patternu: 00 XX 00 00 00 YY YY YY
        // Gdzie XX - to dlugosc tekstu w oknie dialogowym
        // 00 - sa schematem
        // YY - tekst wlasciwy o dlugosci XX

        ArrayList<Character> toCheck = getFilePart(position, array);
        if (toCheck.size() != 5) return false;

        if (toCheck.get(0) != 0) return false;
        if (toCheck.get(1) == 0) return false;
        if (toCheck.get(2) != 0) return false;
        if (toCheck.get(3) != 0) return false;
        if (toCheck.get(4) != 0) return false;

        return true;
    }

    public static boolean checkCharacterArrayIsFakeDialogGame(ArrayList<Character> characters) {

        // Too short to be a dialogue
        if (characters.size() <= 7) return true;

        // For phrases such as 'oDialogueTree'
        if (characters.get(5) == 'o' && Character.isUpperCase(characters.get(6))) return true;

        // For phrases such as 'wKnockMuffled'
        if (characters.get(5) == 'w' && Character.isUpperCase(characters.get(6))) return true;

        // For phrases such as 'sEfDust1'
        if (characters.get(5) == 's' && Character.isUpperCase(characters.get(6))) return true;

        // For phrases such as 'pTrigger'
        if (characters.get(5) == 'p' && Character.isUpperCase(characters.get(6))) return true;

        // For phrases such as 'bGraffiti'
        if (characters.get(5) == 'b' && Character.isUpperCase(characters.get(6))) return true;

        // For phrases such as 'rInit'
        if (characters.get(5) == 'r' && Character.isUpperCase(characters.get(6))) return true;

        // For phrases such as 'DBH5_lp'
        if (characters.get(5) == 'D' && characters.get(6) == 'B') return true;

        // For phrases such as 'bgGodMoon'
        if (characters.get(5) == 'b' && characters.get(6) == 'g' && Character.isUpperCase(characters.get(7)))
            return true;

        // For mark files with extensions
        List<String> extensions = Arrays.asList(".bmp", ".dll", ".wav", ".gml", ".txt", ".exe", ".ogg", ".nc", ".sav", ".ini", ".png");
        for (String extension : extensions) {
            if (characters.stream().map(Object::toString).collect(Collectors.joining()).contains(extension))
                return true;
        }

        // For mark phrases such as 'gml_Room_rm_dock_Create'
        for (int i = 5; i < characters.size(); i++) {
            if (characters.get(i) < 31 || characters.get(i) > 126) return true;
            if (characters.get(i) == '_') return true;
        }
        return false;
    }

}