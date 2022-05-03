package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GameDialogTest {

    private GameDialog exampleDialog;
    private ArrayList<Character> exampleRawGameDialog;

    @Before
    public void setUp() throws Exception {

        // RECOGNIZED PART OF RAW FILE
        exampleRawGameDialog = new ArrayList<>();
        Character[] characters = {0,'e',0,0,0,'A','n','o','t','h','e','r',' ','n','i','g','h','t','.'};
        exampleRawGameDialog.addAll(Arrays.stream(characters).collect(Collectors.toList()));

        // PREPARE RAW GAME DIALOG
        String length = Integer.toHexString(14);
        String exampleText = "Another night.";
        Character[] exampleHeader = {0, length.charAt(0), 0,0,0};

        exampleDialog = new GameDialog();
        exampleDialog.setHeader(exampleHeader);
        exampleDialog.setContent(exampleText);
    }

    @Test
    public void getRawGameDialog() throws Exception {
        Assert.assertEquals(exampleRawGameDialog, exampleDialog.getRawGameDialog());
    }

    @Test
    public void getHeader() {
    }

    @Test
    public void getContent() {
    }

    @Test
    public void setHeader() {
    }

    @Test
    public void setContent() {
    }
}