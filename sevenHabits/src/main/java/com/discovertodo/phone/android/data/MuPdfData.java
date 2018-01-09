package com.discovertodo.phone.android.data;

import android.app.Activity;
import android.net.Uri;

import com.artifex.mupdf.viewer.MuPDFCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ominext on 1/9/2018.
 */

public class MuPdfData {
    public static Uri getUriFromFileCopy(Activity activity){
        try {
            String outFileName = activity.getApplicationInfo().dataDir + "/databases/android_tutorial.pdf";
            File f = new File(outFileName);
            if(!f.exists()) {
                InputStream e = activity.getAssets().open("android_tutorial.pdf");
                File folder = new File(activity.getApplicationInfo().dataDir + "/databases/");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                FileOutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = e.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                e.close();
                return Uri.fromFile(f);
            }else {
                return Uri.fromFile(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static MuPDFCore openFile(MuPDFCore core,String path) {
        int lastSlashPos = path.lastIndexOf('/');
        String mFileName = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        System.out.println("Trying to open " + path);
        try {
            core = new MuPDFCore(path);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } catch (java.lang.OutOfMemoryError e) {
            //  out of memory is not an Exception, so we catch it separately.
            System.out.println(e);
            return null;
        }
        return core;
    }
    public static MuPDFCore openBuffer(MuPDFCore core,byte buffer[], String magic) {
        System.out.println("Trying to open byte buffer");
        try {
            core = new MuPDFCore(buffer, magic);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return core;
    }
}
