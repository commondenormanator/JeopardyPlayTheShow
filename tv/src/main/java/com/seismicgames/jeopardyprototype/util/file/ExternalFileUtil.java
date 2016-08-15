package com.seismicgames.jeopardyprototype.util.file;

import android.content.Context;
import android.os.Build;
import android.support.annotation.WorkerThread;

import com.theplatform.pdk.smil.api.shared.data.Meta;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jduffy on 7/28/16.
 */
public class ExternalFileUtil {

    public static final String VideoFileName = "video.mp4";
    public static final String GameFileName = "game.csv";
    public static final String MetaFileName = "meta.csv";
    public static final String MarkerFileName = "marker.csv";

    private static final String ReadMeText = "Place a video, game, and meta file in the directory you found this readme to.  The file names must be video.mp4, game.cvs and meta.csv.";

    @WorkerThread
    public static List<File> getEpisodeList(Context context){

        List<File> episodeDirs = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (File externalDir : context.getExternalFilesDirs(null)) {
                if(externalDir == null) continue;
                episodeDirs.addAll(getEpisodeList(externalDir));
            }
        } else {
            episodeDirs.addAll(getEpisodeList(context.getExternalFilesDir(null)));
        }

        return episodeDirs;
    }

    private static List<File> getEpisodeList(File directory){
        if(!directory.isDirectory()) return Collections.emptyList();

        List<File> dirs = new ArrayList<>();
        for (File f : directory.listFiles((FileFilter) DirectoryFileFilter.INSTANCE)) {
            if(isValidEpisodeDir(f)) dirs.add(f);
        }
        return dirs;
    }

    private static boolean isValidEpisodeDir(File dir){
        boolean hasVideo = false;
        boolean hasGame = false;
        boolean hasMeta = false;

        for (File f : dir.listFiles()) {
            if(f.getName().equals(VideoFileName)) hasVideo = true;
            if(f.getName().equals(GameFileName)) hasGame = true;
            if(f.getName().equals(MetaFileName)) hasMeta = true;
        }

        return hasVideo && hasGame && hasMeta;
    }


    public static File getFile(Context context, String filePath) {
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (File externalDir : context.getExternalFilesDirs(null)) {
                file = new File(externalDir, filePath);
                if (file.exists()) break;
            }
        } else {
            file = new File(context.getExternalFilesDir(null), filePath);
        }
        if(file == null || !file.exists()) writeReadMe(context);
        return file;
    }

    private static void writeReadMe(Context context){
        File file;
        OutputStream os = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (File externalDir : context.getExternalFilesDirs(null)) {
                file = new File(externalDir, "README.txt");
                try {
                    os = new FileOutputStream(file);
                    IOUtils.write(ReadMeText, os);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    IOUtils.closeQuietly(os);
                }

            }
        } else {
            file = new File(context.getExternalFilesDir(null),  "README.txt");
            try {
                os = new FileOutputStream(file);
                IOUtils.write(ReadMeText, os);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                IOUtils.closeQuietly(os);
            }
        }
    }
}
