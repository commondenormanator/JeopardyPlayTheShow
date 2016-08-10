package com.seismicgames.jeopardyprototype.gameplay.ai;

import com.seismicgames.jeopardyprototype.episode.QuestionInfo;
import com.seismicgames.jeopardyprototype.gameplay.Player;
import com.seismicgames.jeopardyprototype.ui.view.game.PlayerView;

import java.util.Random;


public class AiManager {

    private static Random rand = new Random();

    //todo manage this more with listeners on the player object
    public static void AiBuzzIn(Player player, final PlayerView playerView){
        if(rand.nextBoolean()){
            player.mDidBuzzIn = true;
            playerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playerView.setState(PlayerView.State.BuzzedIn);
                }
            }, 1000);

        }
    }

    public static void AiAnswerLock(Player player, PlayerView playerView){
        if(player.mDidBuzzIn){
            playerView.setState(PlayerView.State.AnswerLocked);
        }
    }

    public static void AiAnswer(Player player, PlayerView playerView, QuestionInfo info){
        if(player.mDidBuzzIn
                && info.value != 0){
            player.mScore += rand.nextBoolean() ? info.value : -info.value;
            playerView.setPlayerScore(player.mScore);
        }
        player.mDidBuzzIn = false;
        playerView.setState(PlayerView.State.Normal);
    }

}
