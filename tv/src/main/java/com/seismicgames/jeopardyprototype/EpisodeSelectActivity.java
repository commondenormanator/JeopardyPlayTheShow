package com.seismicgames.jeopardyprototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;

@BuzzerScene(BuzzerScene.Scene.REMOTE)
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
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        return false;
                    }
                })
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(dialog != null && dialog.isShowing()){
            return dialog.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
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
