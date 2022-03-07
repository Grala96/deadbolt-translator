package hexeditor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

public class FileToHex {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String UNKNOWN_CHARACTER = ".";

    public static void main(String[] args) throws IOException {

        // the stream holding the file content
//        InputStream is = getClass().getClassLoader().getResourceAsStream("file.txt");

        // for static access, uses the class name directly
        InputStream is = FileToHex.class.getClassLoader().getResourceAsStream("data.win");

        String file = "/path/to/text.txt";

        String s = convertFileToHex(Paths.get(file),is);
        System.out.println(s);
    }

    public static String convertFileToHex(Path path, InputStream is) throws IOException {

        StringBuilder result = new StringBuilder();
        StringBuilder result2 = new StringBuilder();
        int value;

        int count = 0;

        ArrayList<Character> byteArray = new ArrayList<>();

        // Wrzuc kazdy bajt jako pojedynczy element do tablicy
        try (InputStream inputStream = is) {
            while ((value = inputStream.read()) != -1) {
                byteArray.add((char) value);
            }
        }

//        for(Integer i = 0; i < byteArray.size() ; i++){
//            result.append(byteArray.get(i).charValue());
//        }

        // Sprawdz kazdy bajt czy przynalezy do patternu
        for(Integer i = 1686688; i<byteArray.size();i++){

            char now = byteArray.get(i);
            if(checkByte(i,byteArray)){
                int length = byteArray.get(i+1);
                for(Integer k = i+5; k<i+5+length;k++){
                    if(k+length>byteArray.size()) continue;
                    result.append(byteArray.get(k));
                }
                result.append("\n");
                count++;
                System.out.println(result);
                result2.append(result);
                result = new StringBuilder();
            }

        }
        return result2.toString();
    }

    public static ArrayList<Character> getFilePart(Integer position, ArrayList<Character> arrayList){
        ArrayList<Character> result = new ArrayList<>();
        for(Integer pos = position; pos <= position + 4 ; pos++) {
            if(pos >arrayList.size()) break;
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


}