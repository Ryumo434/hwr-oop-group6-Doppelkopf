package hwr.oop.doppelkopf.group6;

import static org.assertj.core.api.Assertions.assertThat;

import hwr.oop.doppelkopf.group6.cli.*;
import hwr.oop.doppelkopf.group6.persistence.SaveToFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileTest {
  private OutputStream outputStream;
  CreateCommand command;
  ParseCommand parse;
  String fileName = "doppelkopf.csv";
  Path currentRelativePath = Paths.get("");
  String currentDir = currentRelativePath.toAbsolutePath().toString();
  File file = new File(currentDir + File.separator + fileName);
  Path path = file.toPath();

  @BeforeEach
  void setUp() throws IOException {
    this.outputStream = new ByteArrayOutputStream();
    this.parse = new ParseCommand(outputStream);
    command = new CreateCommand(outputStream, IOExceptionBomb.DONT);

    if (Files.exists(path)) {
      Files.delete(path);
    }
  }

  @AfterEach
  void tearDown() throws IOException {

    if (Files.exists(path)) {
      Files.delete(path);
    }
  }

  @Test
  void testCreateGameWithEmptyLineInFile() throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.newLine();
    }

    List<String> args = new ArrayList<>();
    args.add("game");
    args.add("2");
    args.add("create");
    args.add("josef");
    args.add("anna");
    args.add("jannis");
    args.add("lena");

    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    command.execute(args);

    // Check the output to ensure the game creation proceeds
    String output = outputStream.toString().trim();
    assertThat(output).contains("Spiel 2 wird erstellt...");
  }

  @Test
  void testCreateGameWithFileNotExisting() {
    List<String> args = new ArrayList<>();
    args.add("game");
    args.add("1");
    args.add("create");
    args.add("josef");
    args.add("anna");
    args.add("jannis");
    args.add("lena");

    String gameID = parse.gameID(args);
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    command.execute(args);

    assertThat(Files.exists(path)).isTrue();

    String expectedMessage = "Spiel " + gameID + " wird erstellt...";
    String output = outputStream.toString().trim();
    assertThat(output).contains(expectedMessage);
  }

  @Test
  void testCreateGameWithGameExisting() {
    List<String> args = new ArrayList<>();
    args.add("game");
    args.add(null);
    args.add("create");
    args.add("josef");
    args.add("anna");
    args.add("jannis");
    args.add("lena");

    PrintStream printStream = new PrintStream(outputStream);
    System.setErr(printStream);

    command.execute(args);

    String expectedMessage =
        "Game ID: " + "\"" + args.get(1) + "\"" + " is not a valid game ID. Please use numbers!";
    String output = outputStream.toString().trim();
    assertThat(output).contains(expectedMessage);
  }

  @Test
  void testCreateGameWithFileExisting() {
    List<String> args = new ArrayList<>();
    args.add("game");
    args.add("1");
    args.add("create");

    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    command.execute(args);

    assertThat(Files.exists(path)).isTrue();

    String expectedMessage = "Die Datei wird erstellt...";
    String output = outputStream.toString().trim();
    assertThat(output).contains(expectedMessage);
  }

  @Test
  void testCreateGameWhenGameAlreadyExistsIOException() throws IOException {
    InitCommand play =
        new InitCommand(
            IOExceptionBomb.DO,
            outputStream,
            new Deck(),
            new SaveToFile(),
            new ParseCommand(outputStream));
    List<String> args = new ArrayList<>();
    args.add("game");
    args.add("1");
    args.add("create");
    args.add("josef");
    args.add("anna");
    args.add("jannis");
    args.add("lena");

    String gameID = parse.gameID(args);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write("2,maria,hans,fritz,lisa");
      writer.newLine();
      writer.write(gameID);
      writer.newLine();
    }

    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    play.execute(args);

    String expectedMessage = "Caught an IOException: Here goes everything exploding...";
    String output = outputStream.toString().trim();
    assertThat(output).contains(expectedMessage);
  }

  @Test
  void testIOException() {
    command = new CreateCommand(outputStream, IOExceptionBomb.DO);
    List<String> args = new ArrayList<>();
    args.add("game");
    args.add("1");
    args.add("create");

    PrintStream printStream = new PrintStream(outputStream);
    System.setErr(printStream);

    command.execute(args);

    String expectedMessage = "IOException aufgetreten: Here goes everything exploding...";
    String output = outputStream.toString().trim();
    assertThat(output).contains(expectedMessage);
  }

  @Test
  void testIOExceptionWhenFileExists() throws IOException {
    String gameID = "12";
    command = new CreateCommand(outputStream, IOExceptionBomb.DO);
    List<String> args = new ArrayList<>();
    args.add("game");
    args.add("1");
    args.add("create");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(gameID);
      writer.newLine();
    }

    PrintStream printStream = new PrintStream(outputStream);
    System.setErr(printStream);

    command.execute(args);

    String expectedMessage = "IOException aufgetreten: Here goes everything exploding...";
    String output = outputStream.toString().trim();
    assertThat(output).contains(expectedMessage);
  }

  @Test
  void testCreateGameWhenGameAlreadyExists() throws IOException {
    CreateCommand create = new CreateCommand(outputStream, IOExceptionBomb.DONT);
    List<String> args = new ArrayList<>();
    args.add("game");
    args.add("1");
    args.add("create");
    args.add("josef");
    args.add("anna");
    args.add("jannis");
    args.add("lena");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write("1,josef,anna,jannis,lena");
      writer.newLine();
    }

    PrintStream printStream = new PrintStream(outputStream);
    System.setErr(printStream);

    create.execute(args);

    String expectedMessage =
        "IOException aufgetreten: Das Spiel existiert bereits! Probiere eine andere Spiel ID.";
    String output = outputStream.toString().trim();
    assertThat(output).contains(expectedMessage);
  }
}
