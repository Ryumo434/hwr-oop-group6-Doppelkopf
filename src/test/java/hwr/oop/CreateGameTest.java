package hwr.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateGameTest {

    @Test
    void gameCreated(){
        CreateGame createGame = new CreateGame();

        boolean result = createGame.isCreated();

        Assertions.assertTrue(result);
        //assertThat(result).startsWith("Hello").endsWith("World!");
    }

   /* private String isCreated() {
    }*/
}
