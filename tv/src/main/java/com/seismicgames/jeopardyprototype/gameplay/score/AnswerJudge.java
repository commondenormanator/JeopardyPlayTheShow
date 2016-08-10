package com.seismicgames.jeopardyprototype.gameplay.score;

import android.util.Log;

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
    private QuestionInfo currentQuestion;
    private Integer wager = null;

    public boolean didBuzzIn(){
        return didBuzzIn;
    }
    public void setUserBuzzedIn(){
        didBuzzIn = true;
    }

    public boolean didWager(){
        return wager != null;
    }

    private ScoreChangeListener listener;
    public void setListener(ScoreChangeListener listener) {
        this.listener = listener;
    }

    public void setCurrentQuestion(QuestionInfo info){
        currentQuestion = info;
    }
    public QuestionInfo getCurrentQuestion(){
        return currentQuestion;
    }

    public void setUserAnswers(Collection<String> userAnswers) {
        this.userAnswers.clear();
        this.userAnswers.addAll(userAnswers);
    }

    public void setWager(int wager){
        this.wager = clampWager(wager);
    }

    private int clampWager(int userWager){
        if(currentQuestion.type == QuestionInfo.QuestionType.DD){
            //min wager is 5
            int minWager = 5;
            //max wager is the clue value or the current score
            int maxWager = Math.max(currentQuestion.value, userScore);
            return Math.max(minWager, Math.min(maxWager, userWager));
        } else {
            //min wager is 1
            int minWager = 0;
            //max wager is the current score
            int maxWager = Math.max(0, userScore);
            return Math.max(minWager, Math.min(maxWager, userWager));
        }

    }

    public void scoreAnswer(QuestionInfo info){

        if(currentQuestion != info) Log.e("JUDGE", "Question info mismatch");

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

            int value = wager == null ? info.value : wager;
            int change = value * (wasCorrect ? 1 : -1);
            userScore +=  change;
            if(listener != null) listener.onScoreChange(userScore, change);
        }

        didBuzzIn = false;
        wager = null;
        currentQuestion = null;
    }

    public int getUserScore(){
        return userScore;
    }

    private boolean compareAnswer(String guess, String answer){
        guess = AnswerUtil.normalizeAnswer(guess.toLowerCase());
        answer = AnswerUtil.normalizeAnswer(answer.toLowerCase());

        Log.d("Judge", String.format("comparing %s to %s: %s", guess, answer, guess.contains(answer)));

        return guess.contains(answer);
    }

    public void reset(){
        userScore = 0;
        didBuzzIn = false;
    }

}
