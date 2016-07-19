package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class EpisodeSelectActivity extends Activity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_episode_select);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialog = dialogBuilder
                .setMessage(R.string.start_episode_prompt)
                .setPositiveButton(R.string.ok, onEpisodeStart())
                .show();
    }

    @Override
    protected void onPause() {
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
        super.onPause();
    }

    private DialogInterface.OnClickListener onEpisodeStart(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(EpisodeSelectActivity.this, GameActivity.class));
            }
        };
    }
}
