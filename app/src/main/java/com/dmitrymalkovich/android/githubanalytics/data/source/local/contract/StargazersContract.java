package com.dmitrymalkovich.android.githubanalytics.data.source.local.contract;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.dmitrymalkovich.android.githubanalytics.data.source.remote.gson.ResponseStargazers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StargazersContract {
    public static final String LOG_TAG = StargazersContract.class.getSimpleName();
    public static final String CONTENT_AUTHORITY = "com.dmitrymalkovich.android.githubanalytics.data";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_STARGAZERS = "stargazers";

    public static final class Entry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STARGAZERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STARGAZERS;

        public static final String TABLE_NAME = "stargazers";
        public static final String COLUMN_REPOSITORY_KEY = "repository_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] STARTGAZERS_COLUMNS = {
                _ID,
                COLUMN_REPOSITORY_KEY,
                COLUMN_TIMESTAMP
        };

        public static final int COL_ID = 0;
        public static final int COL_REPOSITORY_KEY = 1;
        public static final int COL_TIMESTAMP = 2;

        public static ContentValues buildContentValues(long repositoryId,
                                                       ResponseStargazers responseStargazers) {
            // ISO 8601 to milliseconds
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            long timeInMilliseconds = 0;
            try {
                Date date = df.parse(responseStargazers.getStarredAt());
                timeInMilliseconds = date.getTime();
            } catch (ParseException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_REPOSITORY_KEY, repositoryId);
            contentValues.put(COLUMN_TIMESTAMP, timeInMilliseconds);
            return contentValues;
        }
    }
}