package com.everbluepartners.cordovaingenicosdk.utils;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static void copyFileToSD(Context context, String path, String assetFileName) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = context.getAssets().open(assetFileName);
            File parent = new File(path);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            File file = new File(path, assetFileName);
            if (file.exists()) {
                return;
            }
            outputStream = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
