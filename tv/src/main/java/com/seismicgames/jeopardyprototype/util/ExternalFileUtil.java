package com.seismicgames.jeopardyprototype.util;

import android.content.Context;
import android.os.Build;

import java.io.File;

/**
 * Created by jduffy on 7/28/16.
 */
public class ExternalFileUtil {

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
        return file;
    }
}
