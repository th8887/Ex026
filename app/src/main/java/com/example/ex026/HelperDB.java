package com.example.ex026;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SignalStrength;

import androidx.annotation.Nullable;

import static com.example.ex026.Grades.ID;
import static com.example.ex026.Grades.MARK;
import static com.example.ex026.Grades.QUARTER;
import static com.example.ex026.Grades.SUBJECT;
import static com.example.ex026.Grades.TABLE_GRADES;
import static com.example.ex026.Students.ACTIVE;
import static com.example.ex026.Students.ADDRESS;
import static com.example.ex026.Students.KEY_ID;
import static com.example.ex026.Students.NAME;
import static com.example.ex026.Students.NAMEF;
import static com.example.ex026.Students.NAMEM;
import static com.example.ex026.Students.PHONEF;
import static com.example.ex026.Students.PHONEH;
import static com.example.ex026.Students.PHONEM;
import static com.example.ex026.Students.PHONES;
import static com.example.ex026.Students.TABLE_STUDENTS;

/**
 *  The HelperDB.
 *  @author Tahel Hazan <th8887@bs.amalnet.k12.il>
 *  @version 1.1.6
 *  @since 11.12.2020
 *  HelperDB for tables.
 *
 */
public class HelperDB extends SQLiteOpenHelper {
    /**
     * The constant DATABASE_NAME.
     */
    public static final String DATABASE_NAME="dbstudent.db";
    /**
     * The constant DATABASE_VERSION.
     */
    public static final int DATABASE_VERSION=1;
    /**
     * The Str create.
     */
    String strCreate, /**
     * The Str delete.
     */
    strDelete;

    /**
     * Instantiates a new Helper db.
     *
     * @param context the context
     */
    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        strCreate="CREATE TABLE "+TABLE_STUDENTS;
        strCreate+=" ("+KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+NAME+" TEXT,";
        strCreate+=" "+PHONES+" TEXT,";
        strCreate+=" "+ADDRESS+" TEXT,";
        strCreate+=" "+PHONEH+" TEXT,";
        strCreate+=" "+NAMEM+" TEXT,";
        strCreate+=" "+PHONEM+" TEXT,";
        strCreate+=" "+NAMEF+" TEXT,";
        strCreate+=" "+PHONEF+" TEXT,";
        strCreate+=" "+ACTIVE+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+TABLE_GRADES;
        strCreate+=" ("+Grades.ID+" INTEGER,";
        strCreate+=" "+MARK+" INTEGER,";
        strCreate+=" "+SUBJECT+" TEXT,";
        strCreate+=" "+QUARTER+" INTEGER,";
        strCreate+=" "+Grades.ACTIVE+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        strDelete="DROP TABLE IF EXISTS "+ TABLE_STUDENTS;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+ TABLE_GRADES;
        db.execSQL(strDelete);

        onCreate(db);
    }
}
