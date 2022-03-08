package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Data
public class GameDialog {

    private Character[] header; // Header = 00 XX 00 00 00 - where XX is length of content
    private String content;

    public ArrayList<Character> getRawGameDialog() throws Exception {
        if(header.length != 5 || Integer.toHexString(content.length()).charAt(0) != header[1]) throw new Exception("Header of GameDialog has wrong length!");
        ArrayList<Character> rawGameDialog = new ArrayList<>();
        rawGameDialog.addAll(Arrays.asList(header));
        rawGameDialog.addAll(content.chars().mapToObj(o -> (char) o).collect(Collectors.toList()));
        return rawGameDialog;
    }

    GameDialog(){

    }

    public GameDialog(ArrayList<Character> characters) throws Exception {
        if(characters.size()<6) throw new Exception("Raw text is not game dialog!");
        this.header = new Character[5];
        this.header[0] = characters.get(0);
        this.header[1] = characters.get(1);
        this.header[2] = characters.get(2);
        this.header[3] = characters.get(3);
        this.header[4] = characters.get(4);

        StringBuilder sb = new StringBuilder();

        for(Character c : characters.subList(5, characters.size())){
            sb.append(c);
        }
        this.content = sb.toString();
    }

//    public void setLengthDialogInHeader(int length){
//        this.header[1] = (char)length;
//    }
//
//    public int getLengthDialogInHeader(){
//        return (int)this.header[1];
//    }

}
