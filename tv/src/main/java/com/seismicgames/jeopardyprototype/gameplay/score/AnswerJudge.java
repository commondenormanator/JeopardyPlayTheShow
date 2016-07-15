package com.seismicgames.jeopardyprototype.gameplay.score;

import com.seismicgames.jeopardyprototype.episode.QuestionInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jduffy on 7/7/16.
 */
public class AnswerJudge {

    private int userScore = 0;
    private List<String> userAnswers = new ArrayList<>();
    private boolean didBuzzIn = false;

    public boolean didBuzzIn(){
        return didBuzzIn;
    }
    public void setUserBuzzedIn(){
        didBuzzIn = true;
    }

    private ScoreChangeListener listener;
    public void setListener(ScoreChangeListener listener) {
        this.listener = listener;
    }

    public void setUserAnswers(Collection<String> userAnswers) {
        this.userAnswers.clear();
        this.userAnswers.addAll(userAnswers);
    }

    public void scoreAnswer(QuestionInfo info){

        if(didBuzzIn) {
            boolean wasCorrect = false;
            for (String userAnswer : userAnswers) {
                for (String correctAnswer : info.answers) {
                    if (compareAnswer(userAnswer, correctAnswer)) {
                        wasCorrect = true;
                        break;
                    }
                }
                if(wasCorrect) break;
            }
            int change = info.value * (wasCorrect ? 1 : -1);
            userScore +=  change;
            if(listener != null) listener.onScoreChange(userScore, change);
        }

        didBuzzIn = false;
    }


    private boolean compareAnswer(String guess, String answer){
        guess = guess.toLowerCase();
        answer = answer.toLowerCase();

        return guess.contains(answer);
    }

    public void reset(){
        userScore = 0;
        didBuzzIn = false;
    }

}
