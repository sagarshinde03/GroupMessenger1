package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 * @author stevko
 *
 */
public class GroupMessengerProvider extends ContentProvider {

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // You do not need to implement this.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        /*
        Set<Map.Entry<String, Object>> s=values.valueSet();
        Iterator itr = s.iterator();

        Map.Entry me = (Map.Entry)itr.next();
        Map.Entry me1 = (Map.Entry)itr.next();
        //String key = me.getKey().toString();
        Object value =  me.getValue();
        Object value1 =  me1.getValue();
        String nameOfFile=value.toString();
        String contentOfFile=value1.toString();
        */
        String nameOfFile=values.get("key").toString();
        String contentOfFile=values.get("value").toString();
        try {
            FileOutputStream fos = getContext().openFileOutput(nameOfFile, Context.MODE_PRIVATE);
            //FileOutputStream fos = openFileOutput(nameOfFile, Context.MODE_PRIVATE);
            //FileOutputStream fos = getApplicationContext.openFileOutput(nameOfFile, getActivity().MODE_PRIVATE);

        fos.write(contentOfFile.getBytes());
        fos.close();

        }
        catch (FileNotFoundException e){
            Log.v("errorInInsert","Found File Not Found exception");
        }
        catch (IOException e){
            Log.v("errorInInsert","Found IOexeption");
        }
        /*
        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
        */
        /*
         * TODO: You need to implement this method. Note that values will have two columns (a key
         * column and a value column) and one row that contains the actual (key, value) pair to be
         * inserted.
         * 
         * For actual storage, you can use any option. If you know how to use SQL, then you can use
         * SQLite. But this is not a requirement. You can use other storage options, such as the
         * internal storage option that we used in PA1. If you want to use that option, please
         * take a look at the code for PA1.
         */
        Log.v("insert", values.toString());
        return uri;
    }

    @Override
    public boolean onCreate() {
        // If you need to perform any one-time initialization task, please do it here.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // use android.database.MatrixCursor cursor
        try {
            int n;
            File file = getContext().getFileStreamPath(selection);
            FileInputStream fis = getContext().openFileInput(selection);
            StringBuffer fileContent = new StringBuffer("");
            String stringFileContent;
            // Reference : http://stackoverflow.com/questions/9095610/android-fileinputstream-read-txt-file-to-string
            byte[] buffer = new byte[1024];
            if(file.exists()){

                while ((n = fis.read(buffer)) != -1)
                {
                    fileContent.append(new String(buffer, 0, n));
                }
            }
            stringFileContent=fileContent.toString();
            String[] columnNames = {"key", "value"};
            MatrixCursor matrixCursor = new MatrixCursor(columnNames);
            matrixCursor.addRow(new String[]{selection, stringFileContent });
            return matrixCursor;
        }
        catch (Exception e){}
        /*
         * TODO: You need to implement this method. Note that you need to return a Cursor object
         * with the right format. If the formatting is not correct, then it is not going to work.
         * 
         * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
         * still need to be careful because the formatting might still be incorrect.
         * 
         * If you use a file storage option, then it is your job to build a Cursor * object. I
         * recommend building a MatrixCursor described at:
         * http://developer.android.com/reference/android/database/MatrixCursor.html
         */
        Log.v("query", selection);
        return null;
        //return matrixCursor;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }
}
