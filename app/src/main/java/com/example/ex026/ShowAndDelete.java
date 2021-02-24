package com.example.ex026;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Helper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.ex026.Grades.TABLE_GRADES;
import static com.example.ex026.Students.TABLE_STUDENTS;
/**
 * @author Tahel Hazan <th8887@bs.amalnet.k12.il>
 * @version 1.1.6
 * @since 11.12.2020
 * This activity shows the information from the charts with an option to delete students.
 */
public class ShowAndDelete extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    int op;
    /**
     * @param s - show information based on chosen name in ListView.
     */
    ListView s;
    /**
     * @param n- shows names.
     */
    Spinner n;
    ArrayList<String> na= new ArrayList<String>();
    /**
     * single collects information from one row in the chart.
     */
    String [] single= new String[7];
    /**
     * @param- adp- for names
     * @param- adps- for other information from the name.
     * @param- adpg- for student's grades.
     */
    ArrayAdapter adp,adps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_and_delete);

        s=(ListView) findViewById(R.id.s);
        n=(Spinner) findViewById(R.id.n);

        n.setOnItemSelectedListener(this);

        hlp= new HelperDB(this);

        showNames();
    }

    /**
     * showNames.
     *
     * connects the list of names to the Spinner.
     */
    public void showNames(){
        db=hlp.getWritableDatabase();

        String [] columns= {Students.NAME};
        String selection= Students.ACTIVE+"=?";
        String [] selectionArgs={"1"};
        String groupBy=null;
        String having= null;
        String orderBy=null;
        String limit=null;

        crsr=db.query(TABLE_STUDENTS,columns, selection, selectionArgs, groupBy,having, orderBy,limit);
        int col1= crsr.getColumnIndex(Students.NAME);
        crsr.moveToFirst();
        while (!crsr.isAfterLast()){
            String names=crsr.getString(col1);

            String tmp=names;
            na.add(tmp);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp= new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,na);
        n.setAdapter(adp);
    }

    public void delete(View view) {
        ContentValues cv= new ContentValues();
        db = hlp.getWritableDatabase();
        cv.put(Students.NAME,na.get(op));
        cv.put(Students.ACTIVE,0);
        db.update(TABLE_STUDENTS,cv, Students.NAME+"=?", new String [] {na.get(op)});
        db.close();

        db=hlp.getWritableDatabase();
        String [] columns= {Students.KEY_ID};
        String selection= Students.NAME+"=?";
        String [] selectionArgs={na.get(op)};
        String groupBy=null;
        String having= null;
        String orderBy=null;
        String limit=null;
        db=hlp.getWritableDatabase();
        crsr=db.query(TABLE_STUDENTS,columns, selection, selectionArgs, groupBy,having, orderBy,limit);
        int col1= crsr.getColumnIndex(Students.KEY_ID);
        crsr.moveToFirst();
        String k=crsr.getString(col1);
        crsr.close();
        db.close();

        na.remove(op);
        adp.notifyDataSetChanged();

        cv.clear();
        db=hlp.getWritableDatabase();
        cv.put(Grades.ID,Integer.parseInt(k));
        cv.put(Grades.ACTIVE,0);
        db.update(TABLE_GRADES, cv, Grades.ID+"=?", new String [] {k});
    }

    @Override
    public void onItemSelected(AdapterView<?> par, View v, int pos, long id) {
        op=pos;
        db = hlp.getWritableDatabase();
        String[] columns = {Students.PHONES, Students.ADDRESS, Students.PHONEH, Students.NAMEM, Students.PHONEM, Students.NAMEF, Students.PHONEF};
        String selection = Students.KEY_ID + "=?";
        String[] selectionArgs = {Integer.toString(pos+1)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        crsr = db.query(TABLE_STUDENTS, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        int col1 = crsr.getColumnIndex(Students.PHONES);
        int col2 = crsr.getColumnIndex(Students.ADDRESS);
        int col3 = crsr.getColumnIndex(Students.PHONEH);
        int col4 = crsr.getColumnIndex(Students.NAMEM);
        int col5 = crsr.getColumnIndex(Students.PHONEM);
        int col6 = crsr.getColumnIndex(Students.NAMEF);
        int col7 = crsr.getColumnIndex(Students.PHONEF);
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            String phones = crsr.getString(col1);
            String add = crsr.getString(col2);
            String phoneh = crsr.getString(col3);
            String namem = crsr.getString(col4);
            String phonem = crsr.getString(col5);
            String namef = crsr.getString(col6);
            String phonef = crsr.getString(col7);

            single[0] = phones;
            single[1] = add;
            single[2] = phoneh;
            if ((namem==null)&&(phonem==null)) {
                single[3] = " ";
                single[4]=" ";
            }
            else {
                single[3] = namem;
                single[4] = phonem;
            }
            if ((namef == null) && (phonef == null)) {
                single[5]=" ";
                single[6]=" ";
            }
            else {
                single[5] = namef;
                single[6] = phonef;
            }

            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adps = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, single);
        s.setAdapter(adps);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    /**
     * Fin.
     *
     * @param item
     * Moves the user to Main Activity(filling personal information) activity.
     */
    public void fin(MenuItem item) {
        Intent c= new Intent(this,MainActivity.class);
        startActivity(c);
    }
    /**
     * Fing.
     *
     * @param item
     *
     * Moves user to filling grades activity.
     */
    public void fing(MenuItem item) {
        Intent c= new Intent(this,Filling_Grades.class);
        startActivity(c);
    }
    /**
     * Pn.
     *
     * @param item
     *
     * Moves user to Personal_info activity.
     */
    public void org(MenuItem item) {
        Intent c= new Intent(this,Organize.class);
        startActivity(c);
    }
    /**
     * Qg.
     *
     * @param item
     *
     * The item Sends a Toast to let the user know he is in the current page
     * he chose from the OptionMenu.
     */
    public void sad(MenuItem item) {
        Toast.makeText(this, "You are already here :)", Toast.LENGTH_SHORT).show();
    }

    /**
     * Cred.
     *
     * @param item
     *
     * Moves the user the Credits activity.
     */
    public void cred(MenuItem item) {
        Intent c= new Intent(this,Credits.class);
        startActivity(c);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}