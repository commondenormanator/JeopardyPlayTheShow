package com.seismicgames.jeopardyprototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;

public class EpisodeSelectActivity extends BuzzerActivity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_episode_select);

        //start up buzzer server
        BuzzerConnectionManager.getInstance(getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialog = dialogBuilder
                .setMessage(R.string.start_episode_prompt)
                .setPositiveButton(R.string.ok, onEpisodeStart())
                .setCancelable(false)
                .setOnCancelListener(onCancel())
                .show();
    }

    @Override
    protected void onPause() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        super.onPause();
    }

    private DialogInterface.OnClickListener onEpisodeStart() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(EpisodeSelectActivity.this, GameActivity.class));
            }
        };
    }

    private DialogInterface.OnCancelListener onCancel() {
        return new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        };
    }
}
