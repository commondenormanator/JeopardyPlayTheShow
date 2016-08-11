package com.seismicgames.jeopardyprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;
import com.seismicgames.jeopardyprototype.util.file.ExternalFileUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

@BuzzerScene(BuzzerScene.Scene.REMOTE)
public class EpisodeSelectActivity extends BuzzerActivity {

    private AlertDialog dialog;

    @BindView(R.id.episodeList)
    protected ListView episodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_episode_select);

        ButterKnife.bind(this);

        new EpisodeListLoader().execute();

        //start up buzzer server
        BuzzerConnectionManager.getInstance(getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        dialog = dialogBuilder
//                .setMessage(R.string.start_episode_prompt)
//                .setPositiveButton(R.string.ok, onEpisodeStart())
//                .setCancelable(false)
//                .setOnCancelListener(onCancel())
//                .setOnKeyListener(new DialogInterface.OnKeyListener() {
//                    @Override
//                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                        return false;
//                    }
//                })
//                .show();
    }

    @Override
    protected void onPause() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        super.onPause();
    }

    @OnItemClick(R.id.episodeList)
    protected void onItemClick(int position) {
        File dir = (File) episodeList.getAdapter().getItem(position);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialog = dialogBuilder
                .setMessage(R.string.start_episode_prompt)
                .setPositiveButton(R.string.ok, onEpisodeStart(dir))
                .setCancelable(true)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        return false;
                    }
                })
                .show();
    }


    @Override
    public void onBackPressed() {
        //ignore back button to avoid demo fumbling
        //super.onBackPressed();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(dialog != null && dialog.isShowing()){
            return dialog.dispatchKeyEvent(event);
        }

        return super.dispatchKeyEvent(event);
    }


    private DialogInterface.OnClickListener onEpisodeStart(final File episodePath) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GameActivity.show(EpisodeSelectActivity.this, episodePath);
            }
        };
    }

    private class EpisodeListLoader extends AsyncTask<Void, Void, EpisodeListAdapter>{

        @Override
        protected EpisodeListAdapter doInBackground(Void... voids) {
            return new EpisodeListAdapter(getApplicationContext(), ExternalFileUtil.getEpisodeList(getApplicationContext()));
        }

        @Override
        protected void onPostExecute(EpisodeListAdapter episodeListAdapter) {
            super.onPostExecute(episodeListAdapter);
            episodeList.setAdapter(episodeListAdapter);
        }
    }

    private static class EpisodeListAdapter extends BaseAdapter{

        private final List<File> files;

        private LayoutInflater mInflater;

        private EpisodeListAdapter(Context context, List<File> files) {
            this.files = files;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int i) {
            return files.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            View v = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            ((TextView)v.findViewById(android.R.id.text1)).setText(files.get(i).getName());

            return v;
        }
    }

}
