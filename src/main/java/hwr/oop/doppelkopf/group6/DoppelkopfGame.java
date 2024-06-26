package hwr.oop.doppelkopf.group6;

import java.util.*;

public class DoppelkopfGame {
  public final List<Player> players = new ArrayList<>();
  public final Deck deck = new Deck();
  public final Stich currentStich = new Stich();

  public DoppelkopfGame() {
    initializePlayers();
  }

  public void createDeck() {
    deck.initializeCards();
    deck.shuffleDeck();
    deck.dealCards(players);
  }

  public void setPlayerGroups() {
    for (Player player : players) {
      player.setGroup();
    }
  }

  public void oneGame() {
    int winner = oneRound() - 1;
    boolean hochzeit =
        !(players.stream()
            .filter(player -> player.getGroup().equals(PlayerGroup.HOCHZEIT))
            .toList()
            .isEmpty());
    if (hochzeit && (players.get(winner).getGroup() != PlayerGroup.HOCHZEIT)) {
      for (Player player : players) {
        if (player.getGroup().equals(PlayerGroup.HOCHZEIT)) {
          player.setGroup(PlayerGroup.RE);
        }
      }
      players.get(winner).setGroup(PlayerGroup.RE);
    }
  }

  public void initializePlayers() {
    players.add(new Player("Spieler1", 1, 0));
    players.add(new Player("Spieler2", 2, 0));
    players.add(new Player("Spieler3", 3, 0));
    players.add(new Player("Spieler4", 4, 0));
  }

  public int oneRound() {
    players.get(0).getHand().playCard(0, currentStich);
    players.get(1).getHand().playCard(0, currentStich);
    players.get(2).getHand().playCard(0, currentStich);
    players.get(3).getHand().playCard(0, currentStich);
    int winner = currentStich.findHighestCard();
    addRoundPoints(currentStich);
    currentStich.resetStich();
    return winner;
  }

  public void addRoundPoints(Stich stich) {
    PlayerGroup winnerTeam = players.get(stich.getWinnerPos()).getGroup();
    for (Player player : players) {
      if (player.getGroup().equals(winnerTeam)) {
        player.addPoints(stich.getPoints());
      }
    }
  }

  public void switchPlayerCardsDuringPoverty(
      List<Card> poorPlayerCards,
      int poorPlayerIndex,
      List<Card> richPlayerCards,
      int richPlayerIndex) {
    for (Card i : poorPlayerCards) {
      players.get(poorPlayerIndex).getOwnCards().removeIf(i::equals);
      players.get(richPlayerIndex).getOwnCards().add(i);
    }

    for (Card i : richPlayerCards) {
      players.get(richPlayerIndex).getOwnCards().removeIf(i::equals);
      players.get(poorPlayerIndex).getOwnCards().add(i);
    }
  }

  public boolean checkForPoverty(List<Player> player) {
    for (Player i : player) {
      if (i.getHand().countPlayersTrumpCards() <= 3) {
        return true;
      }
    }
    return false;
  }

  public DoppelkopfGame playCard(Player player, String shortcut) {
    player.getHand().playCard(shortcut, currentStich);
    if (currentStich.getCards().size() == 4) {
      currentStich.findHighestCard();
      currentStich.updateWinnerPos((player.getOrder()) % 4);
      addRoundPoints(currentStich);
      currentStich.resetStich();
    }
    return this;
  }
}
