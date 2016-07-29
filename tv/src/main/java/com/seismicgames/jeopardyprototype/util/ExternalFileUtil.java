package com.seismicgames.jeopardyprototype.util;

import android.content.Context;
import android.os.Build;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by jduffy on 7/28/16.
 */
public class ExternalFileUtil {

    private static final String ReadMeText = "Place a video, game, and meta file in the directory you found this readme to.  The file names must be video.mp4, game.cvs and meta.csv.";

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
