package hwr.oop.doppelkopf;


class StartGame {

  static DoppelkopfGame game = new DoppelkopfGame();

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    if(args[0].equals("print")) {

      System.out.println(game.player1.getOwnCards());
    }
  }
}
