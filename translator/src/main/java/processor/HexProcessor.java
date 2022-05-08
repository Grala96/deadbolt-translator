package hexeditor;

import model.PartData;
import utils.CheckSum;

import java.util.ArrayList;
import java.util.List;

import static model.DataType.*;

public class HexProcessor {

    public static final int HEADER_OFFSET = 5;

    public static ArrayList<PartData> processData(ArrayList<Character> characterArrayList, String source) {

        ArrayList<PartData> structuredData = new ArrayList<>();

        PartData currentPartData = new PartData();
        ArrayList<Character> tempList = new ArrayList<>();

        // Sprawdz kazdy bajt czy przynalezy do patternu
        // Iteruj po wszystkich bajtach jeden po drugim
        for (Integer i = 0; i < characterArrayList.size(); i++) {

            char currentByte = characterArrayList.get(i);

            // Sprawdz bajt (i nastepne) czy przynalezy do patternu
            if (checkByte(i, characterArrayList)) {

                // Jesli przynalezy to:
                // - tymczasowo trzymane dane wrzuc do currentPartData o ile tempList nie jest pusty
                // - oznacz je jako nierozpoznane
                // - wrzuc calosc do finalnej struktury

                if (!tempList.isEmpty()) {
                    String checkSum = CheckSum.sha256(tempList);
                    currentPartData.setData(tempList);
                    currentPartData.setType(UNKNOWN_DATA);
                    currentPartData.setSourceFile(source);
                    currentPartData.setDataChecksum(checkSum);
                    structuredData.add(currentPartData);
                }

                // - wyczysc currentPartData, wyczysc tempList

                currentPartData = new PartData();
                tempList = new ArrayList<>();

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

                // Dopisujemy sume kontrolna
                String checkSum = CheckSum.sha256(tempList);
                currentPartData.setDataChecksum(checkSum);

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
        for (Integer i = 0; i < structuredData.size(); i++) {
            structuredData.get(i).setPartId(i);
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
        if (characters.size() <= 7) return true;
        if (characters.get(5) == 'o' && Character.isUpperCase(characters.get(6))) return true;
        if (characters.get(5) == 'w' && Character.isUpperCase(characters.get(6))) return true;
        if (characters.get(5) == 's' && Character.isUpperCase(characters.get(6))) return true;
        if (characters.get(5) == 'p' && Character.isUpperCase(characters.get(6))) return true;
        if (characters.get(5) == 'b' && Character.isUpperCase(characters.get(6))) return true;
        if (characters.get(5) == 'D' && characters.get(6) == 'B') return true;
        for (int i = 5; i < characters.size(); i++) {
            if (characters.get(i) < 31 || characters.get(i) > 126) return true;
            if (characters.get(i) == '_') return true;

        }
        return false;
    }

}