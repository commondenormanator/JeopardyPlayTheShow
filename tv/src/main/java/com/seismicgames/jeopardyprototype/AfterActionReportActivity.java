package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AfterActionReportActivity extends Activity {

    private static final String EXTRA_PLAYER1_SCORE = "EXTRA_PLAYER1_SCORE";
    private static final String EXTRA_PLAYER2_SCORE = "EXTRA_PLAYER2_SCORE";
    private static final String EXTRA_PLAYER3_SCORE = "EXTRA_PLAYER3_SCORE";

    @BindView(R.id.winner1)
    ViewGroup winner1;
    @BindView(R.id.winner2)
    ViewGroup winner2;
    @BindView(R.id.winner3)
    ViewGroup winner3;

    public static void ShowPostMatch(Context context, int player1Score, int player2Score, int player3Score) {
        Intent intent = new Intent(context, AfterActionReportActivity.class);
        intent.putExtra(EXTRA_PLAYER1_SCORE, player1Score);
        intent.putExtra(EXTRA_PLAYER2_SCORE, player2Score);
        intent.putExtra(EXTRA_PLAYER3_SCORE, player3Score);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_action_report);

        int player1 = getIntent().getIntExtra(EXTRA_PLAYER1_SCORE, 0);
        int player2 = getIntent().getIntExtra(EXTRA_PLAYER2_SCORE, 0);
        int player3 = getIntent().getIntExtra(EXTRA_PLAYER3_SCORE, 0);

        ButterKnife.bind(this);

        ((TextView)winner1.findViewById(R.id.userScore)).setText(String.format("$%d", player1));
        ((TextView)winner2.findViewById(R.id.userScore)).setText(String.format("$%d", player2));
        ((TextView)winner3.findViewById(R.id.userScore)).setText(String.format("$%d", player3));

        ((ImageView)winner1.findViewById(R.id.playerAvatar)).setImageResource(R.drawable.avatar_player1);
        ((ImageView)winner2.findViewById(R.id.playerAvatar)).setImageResource(R.drawable.avatar_player2);
        ((ImageView)winner3.findViewById(R.id.playerAvatar)).setImageResource(R.drawable.avatar_player3);


        winner1.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },
                15000);
    }

}
