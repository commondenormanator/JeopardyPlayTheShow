package com.seismicgames.jeopardyprototype.gameplay.engine;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jduffy on 6/29/16.
 */
public class GameEngine {


    private final List<GameObject> gameObjects = Collections.synchronizedList(new ArrayList<GameObject>());

    private final GameThread thread = new GameThread();

    public Handler handler = new Handler(Looper.getMainLooper());

    private Activity currentActivity;


    public void addGameObject(GameObject go) {
        gameObjects.add(go);
    }

    public void removeGameObject(GameObject go) {
        gameObjects.remove(go);
    }

    public void registerActivity(Activity a){
        currentActivity = a;
    }
    public void unregisterActivity(Activity a){
        currentActivity = null;
    }

    public Activity getCurrentActivity(){
        return currentActivity;
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        thread.stopped = true;
    }


    class GameThread extends Thread {

        private static final int maxFPS = 60;
        private static final int minFrameDuration = 1000 / 60;

        public volatile boolean stopped;

        public void run() {

            while (!stopped) {
                long start = SystemClock.uptimeMillis();
                for (GameObject go : gameObjects) {
                    go.pump();
                }
                try {
                    Thread.sleep(Math.max(0, (SystemClock.uptimeMillis() - start - minFrameDuration)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
