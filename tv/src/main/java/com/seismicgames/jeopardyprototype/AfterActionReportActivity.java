package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AfterActionReportActivity extends Activity {

    private static final String EXTRA_USER_SCORE = "EXTRA_USER_SCORE";

    @BindView(R.id.userScore)
    TextView userScoreTextView;

    public static void ShowPostMatch(Context context, int userScore) {
        Intent intent = new Intent(context, AfterActionReportActivity.class);
        intent.putExtra(EXTRA_USER_SCORE, userScore);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_action_report);

        int userScore = getIntent().getIntExtra(EXTRA_USER_SCORE, 0);

        ButterKnife.bind(this);

        userScoreTextView.setText(String.format("$%d", userScore));

        userScoreTextView.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },
                15000);
    }

}
