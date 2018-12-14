package com.p.suraj.photos1;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Filee {

    private static final int EOF = -1;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;



    private Filee() {



    }



    public static java.io.File from(Context context, Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);

        String fileName = getFileName(context, uri);

        String[] splitName = splitFileName(fileName);

        java.io.File tempFile = java.io.File.createTempFile(splitName[0], splitName[1]);

        tempFile = rename(tempFile, fileName);

        tempFile.deleteOnExit();

        FileOutputStream out = null;

        try {

            out = new FileOutputStream(tempFile);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        if (inputStream != null) {

            copy(inputStream, out);

            inputStream.close();

        }



        if (out != null) {

            out.close();

        }

        return tempFile;

    }



    private static String[] splitFileName(String fileName) {

        String name = fileName;

        String extension = "";

        int i = fileName.lastIndexOf(".");

        if (i != -1) {

            name = fileName.substring(0, i);

            extension = fileName.substring(i);

        }



        return new String[]{name, extension};

    }



    private static String getFileName(Context context, Uri uri) {

        String result = null;

        if (uri.getScheme().equals("content")) {

            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

            try {

                if (cursor != null && cursor.moveToFirst()) {

                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                if (cursor != null) {

                    cursor.close();

                }

            }

        }

        if (result == null) {

            result = uri.getPath();

            int cut = result.lastIndexOf(java.io.File.separator);

            if (cut != -1) {

                result = result.substring(cut + 1);

            }

        }

        return result;

    }



    private static java.io.File rename(java.io.File file, String newName) {

        java.io.File newFile = new java.io.File(file.getParent(), newName);

        if (!newFile.equals(file)) {

            if (newFile.exists() && newFile.delete()) {

                Log.d("Filee", "Delete old " + newName + " file");

            }

            if (file.renameTo(newFile)) {

                Log.d("Filee", "Rename file to " + newName);

            }

        }

        return newFile;

    }



    private static long copy(InputStream input, OutputStream output) throws IOException {

        long count = 0;

        int n;

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        while (EOF != (n = input.read(buffer))) {

            output.write(buffer, 0, n);

            count += n;

        }

        return count;

    }

}