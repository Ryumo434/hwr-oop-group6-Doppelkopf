package hwr.oop;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CreateGame {

    boolean isCreated(){
        String filePath = "C:\\Users\\nasse\\Desktop\\testdDhatei.txt"; // Pfad zur Datei
        String content = "heyo";

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Die Datei existiert nicht.");
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Der zweite Parameter "true" weist FileWriter an, den Inhalt an das Ende der Datei anzuhängen
            writer.write(content);
            System.out.println("Text wurde erfolgreich in die Datei geschrieben.");
            return true;
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben in die Datei: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {


    CreateGame createGame = new CreateGame();
    createGame.isCreated();


    }




}
