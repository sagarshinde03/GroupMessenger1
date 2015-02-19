package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by sagar on 2/17/15.
 */
public class OnSendClickListener implements View.OnClickListener {

    private static final String TAG = OnSendClickListener.class.getName();
    private static final int TEST_CNT = 50;
    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";

    private final TextView mTextView;
    private final EditText mEditText;
    private final ContentResolver mContentResolver;
    private final Uri mUri;
    private final ContentValues[] mContentValues;
    private final ContentValues newContentValues=new ContentValues();

    public OnSendClickListener(EditText _et, TextView _tv, ContentResolver _cr) {
        mEditText = _et;
        mTextView = _tv;
        mContentResolver = _cr;
        mUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger1.provider");
        mContentValues = initTestValues();
        newContentValues.put(KEY_FIELD,"KEY0");
        newContentValues.put(VALUE_FIELD,"VALUEE0");
    }

    /**
     * buildUri() demonstrates how to build a URI for a ContentProvider.
     *
     * @param scheme
     * @param authority
     * @return the URI
     */
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

    private ContentValues[] initTestValues() {
        ContentValues[] cv = new ContentValues[TEST_CNT];
        for (int i = 0; i < TEST_CNT; i++) {
            cv[i] = new ContentValues();
            cv[i].put(KEY_FIELD, "key" + Integer.toString(i));
            cv[i].put(VALUE_FIELD, "val" + Integer.toString(i));
        }

        return cv;
    }

    @Override
    public void onClick(View v) {
        if (testInsert()) {
            mTextView.append("Insert success\n");
        } else {
            mTextView.append("Insert fail\n");
            return;
        }

        if (testQuery()) {
            mTextView.append("Query success\n");
        } else {
            mTextView.append("Query fail\n");
        }
    }

    /**
     * testInsert() uses ContentResolver.insert() to insert values into your ContentProvider.
     *
     * @return true if the insertions were successful. Otherwise, false.
     */
    private boolean testInsert() {
        try {
            String msg = mEditText.getText().toString() + "\n";
            mEditText.setText("");

            // logic to check whether the file already exists
            // Reference : http://stackoverflow.com/questions/10576930/trying-to-check-if-a-file-exists-in-internal-storage
            //File file = getBaseContext().getFileStreamPath("0");
            //FileOutputStream fos = getContext().openFileOutput(nameOfFile, Context.MODE_PRIVATE);
            //FileOutputStream fos = openFileOutput("nameOfFile", Context.MODE_PRIVATE);

            newContentValues.put(KEY_FIELD,"0");
            newContentValues.put(VALUE_FIELD,msg);

            mContentResolver.insert(mUri, newContentValues);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }

        return true;
    }

    /**
     * testQuery() uses ContentResolver.query() to retrieves values from your ContentProvider.
     * It simply queries one key at a time and verifies whether it matches any (key, value) pair
     * previously inserted by testInsert().
     *
     * Please pay extra attention to the Cursor object you return from your ContentProvider.
     * It should have two columns; the first column (KEY_FIELD) is for keys
     * and the second column (VALUE_FIELD) is values. In addition, it should include exactly
     * one row that contains a key and a value.
     *
     * @return
     */
    private boolean testQuery() {
        try {
            for (int i = 0; i < TEST_CNT; i++) {
                String key = (String) mContentValues[i].get(KEY_FIELD);
                String val = (String) mContentValues[i].get(VALUE_FIELD);

                Cursor resultCursor = mContentResolver.query(mUri, null, key, null, null);
                if (resultCursor == null) {
                    Log.e(TAG, "Result null");
                    throw new Exception();
                }

                int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
                int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
                if (keyIndex == -1 || valueIndex == -1) {
                    Log.e(TAG, "Wrong columns");
                    resultCursor.close();
                    throw new Exception();
                }

                resultCursor.moveToFirst();

                if (!(resultCursor.isFirst() && resultCursor.isLast())) {
                    Log.e(TAG, "Wrong number of rows");
                    resultCursor.close();
                    throw new Exception();
                }

                String returnKey = resultCursor.getString(keyIndex);
                String returnValue = resultCursor.getString(valueIndex);
                if (!(returnKey.equals(key) && returnValue.equals(val))) {
                    Log.e(TAG, "(key, value) pairs don't match\n");
                    resultCursor.close();
                    throw new Exception();
                }

                resultCursor.close();
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
