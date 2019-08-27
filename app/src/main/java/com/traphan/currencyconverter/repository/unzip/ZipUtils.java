package com.traphan.currencyconverter.repository.unzip;

import android.os.Environment;
import io.reactivex.Single;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    public Single<Map<String, String>> unpackZip(InputStream inputStream) {
        InputStream is;
        ZipInputStream zis;
        Map<String, String> patches = new HashMap();
        try
        {
            String filename;
            is = inputStream;
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + filename) + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + filename);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }
                patches.put(filename.substring(0, filename.length() - 4),Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + filename);
                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e) {
            e.printStackTrace();
            return Single.just(patches);
        }
        return Single.just(patches);
    }
}
