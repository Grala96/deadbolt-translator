package hexeditor;

import com.opencsv.CSVWriter;
import model.DataType;
import model.GameDialog;
import model.PartData;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileToHex {

    public static void main(String[] args) throws IOException {

        InputStream inputStreamDataWin = FileToHex.class.getClassLoader().getResourceAsStream("data.win.original");
        InputStream inputStreamDeadboltGameExe = FileToHex.class.getClassLoader().getResourceAsStream("data.win.original");

        ArrayList<Character> characters = convertFileToCharacterArray(inputStreamDataWin);

        ArrayList<PartData> structuredData = processData(characters);

        saveCharactersToFile(characters,"new.data.win");

        saveGameDialogsToCsv(structuredData, "dialogs.csv");

//        System.out.println(Arrays.toString(characters.toArray()));
    }

    public static ArrayList<Character> convertFileToCharacterArray(InputStream is) throws IOException {

        int value;
        ArrayList<Character> characterArrayList = new ArrayList<>();

        // Wrzuc kazdy bajt / character jako pojedynczy element do tablicy
        try (InputStream inputStream = is) {
            while ((value = inputStream.read()) != -1) {
                characterArrayList.add((char) value);
            }
        }

        return characterArrayList;
    }

    public static ArrayList<PartData> processData(ArrayList<Character> characterArrayList){

        ArrayList<PartData> structuredData = new ArrayList<>();

        PartData currentPartData = new PartData();
        ArrayList<Character> tempList = new ArrayList<>();

        // Sprawdz kazdy bajt czy przynalezy do patternu
        // Iteruj po wszystkich bajtach jeden po drugim
        for(Integer i = 0; i < characterArrayList.size() ; i++){

            char currentByte = characterArrayList.get(i);

            // Sprawdz bajt (i nastepne) czy przynalezy do patternu
            if(checkByte(i,characterArrayList)){

                // Jesli przynalezy to:
                // - tymczasowo trzymane dane wrzuc do currentPartData o ile tempList nie jest pusty
                // - oznacz je jako nierozpoznane
                // - wrzuc calosc do finalnej struktury

                if(!tempList.isEmpty()){
                    currentPartData.setData(tempList);
                    currentPartData.setType(DataType.UNKNOWN_DATA);
                    structuredData.add(currentPartData);
                }

                // - wyczysc currentPartData, wyczysc tempList

                currentPartData = new PartData();
                tempList = new ArrayList<>();

                // - zapakuj szukane dane do currentPartData (subList zgodnie z iloscia zadeklarowana w header)
                int lengthOfRecognitionGameDialog = characterArrayList.get(i+1);
                List<Character> recognizedGameDialog = characterArrayList.subList(i,i+5+lengthOfRecognitionGameDialog);
                currentPartData.setData(new ArrayList<>(recognizedGameDialog));

                // - oznacz je jako znany game dialog
                if(checkCharacterArrayIsFakeDialogGame(currentPartData.getData())){
                    currentPartData.setType(DataType.FAKE_DIALOG_GAME);
                } else {
                    currentPartData.setType(DataType.DIALOG_GAME);
                }


                // - wrzuc do final structure
                structuredData.add(currentPartData);

                // - przesun index odpowiednio na nastepne dane
                // - przesuwamy + 5 - taka dlugosc ma header
                // - przesuwamy + dlugosc game dialog
                // - przesuwamy - 1 - ze wzgledu na inkrementacje w petli
                i+=5+lengthOfRecognitionGameDialog-1;

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

            if(i == characterArrayList.size()-1){
                currentPartData.setData(tempList);
                currentPartData.setType(DataType.UNKNOWN_DATA);
                structuredData.add(currentPartData);
            }

        }

        // przeedytuj wszystkie obiekty nadajac im id zgodnie z kolejnoscia w tablicy
        for(Integer i = 0; i<structuredData.size();i++){
            structuredData.get(i).setPartId(i);
        }

        return structuredData;
    }

    public static ArrayList<Character> getFilePart(Integer position, ArrayList<Character> arrayList){
        ArrayList<Character> result = new ArrayList<>();
        for(Integer pos = position; pos < position + 5 && pos < arrayList.size() ; pos++) {
            result.add(arrayList.get(pos));
        }
        return result;
    }

    public static boolean checkByte(Integer position, ArrayList<Character> array){
        // Szukamy rozpoczęcia dla patternu: 00 XX 00 00 00 YY YY YY
        // Gdzie XX - to dlugosc tekstu w oknie dialogowym
        // 00 - sa schematem
        // YY - tekst wlasciwy o dlugosci XX

        ArrayList<Character> toCheck = getFilePart(position, array);
        if(toCheck.size() != 5) return false;

        if(toCheck.get(0) != 0) return false;
        if(toCheck.get(1) == 0) return false;
        if(toCheck.get(2) != 0) return false;
        if(toCheck.get(3) != 0) return false;
        if(toCheck.get(4) != 0) return false;

        return true;
    }

    public static void saveCharactersToFile(ArrayList<Character> characters, String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(fileOutputStream));

        for(Character c : characters){
            dataOutputStream.write(c);
        }
        dataOutputStream.close();
    }

    public static void saveGameDialogsToCsv(ArrayList<PartData> data, String fileName){
        List<String[]> gameDialogList = new ArrayList<>();
        String[] header = {"dataPartId","gameDialog"};
        gameDialogList.add(header);

        for(PartData partData : data){
            if(partData.getType().equals(DataType.DIALOG_GAME)){
                try {
                    GameDialog tempGameDialog = new GameDialog(partData.getData());
                    String[] temp = {String.valueOf(partData.getPartId()),tempGameDialog.getContent()};
                    gameDialogList.add(temp);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        try(CSVWriter writer = new CSVWriter(new FileWriter(fileName))){
            writer.writeAll(gameDialogList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean checkCharacterArrayIsFakeDialogGame(ArrayList<Character> characters) {
        if(characters.size() <= 7) return true;
        if(characters.get(5) == 'o' && Character.isUpperCase(characters.get(6))) return true;
        if(characters.get(5) == 'w' && Character.isUpperCase(characters.get(6))) return true;
        if(characters.get(5) == 's' && Character.isUpperCase(characters.get(6))) return true;
        if(characters.get(5) == 'p' && Character.isUpperCase(characters.get(6))) return true;
        if(characters.get(5) == 'b' && Character.isUpperCase(characters.get(6))) return true;
        if(characters.get(5) == 'D' && characters.get(6) == 'B') return true;
        for(int i = 5; i < characters.size(); i++){
            if(characters.get(i) < 31 || characters.get(i) > 126) return true;
            if(characters.get(i) == '_') return true;

        }
        return false;
    }

}