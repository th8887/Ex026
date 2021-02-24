package com.example.ex026;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

import static com.example.ex026.Grades.TABLE_GRADES;
import static com.example.ex026.Students.TABLE_STUDENTS;
/**
 * @author Tahel Hazan <th8887@bs.amalnet.k12.il>
 * @version 1.1.6
 * @since 11.12.2020
 * This activity organizes information according to user's request.
 */
public class Organize extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SQLiteDatabase db;
    HelperDB hlp;
    /**
     * @param crsr2- for the function getnames
     */
    Cursor crsr,crsr2;
    ListView flist;
    ContentValues cv= new ContentValues();
    String[] request={"select sort way","all subjects, specific student","specific subject, all students."};
    /**
     * @param req- request.
     * @param ob- order by request.
     */
    Spinner req,ob;
    /**
     * @param obl- order by list.
     * shows the list of information according to the req Spinner.
     * @param fl- final list for the ListView.
     */
    ArrayList<String> ln= new ArrayList<String>();
    ArrayList<String> obl= new ArrayList<String>();
    ArrayList<String> fl= new ArrayList<String>();
    /**
     * @param op- refers to position in ob Spinner.
     * @param count- counts the amount of times the user presses the ToggleButton.
     *             0- down.
     *             1- up.
     */
    int op,count;
    /**
     * @param s- for the chosen subject in ob Spinner.
     */
    String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize);

        req=(Spinner) findViewById(R.id.req);
        ob=(Spinner) findViewById(R.id.ob);
        flist=(ListView) findViewById(R.id.flist);

        req.setOnItemSelectedListener(this);
        ob.setOnItemSelectedListener(this);

        hlp= new HelperDB(this);
        db=hlp.getWritableDatabase();
        db.close();

        ArrayAdapter<String> qadp= new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, request);
        req.setAdapter(qadp);
        count=0;
    }


    @Override
    public void onItemSelected(AdapterView<?> par, View v, int pos, long id) {
        switch (par.getId()) {
            case R.id.req:
                if (pos == 1) {
                    obl.clear();
                    obl.add("Names");
                    listByN(obl);
                    ArrayAdapter<String> nadp = new ArrayAdapter<String>(this,
                            R.layout.support_simple_spinner_dropdown_item, obl);
                    ob.setAdapter(nadp);
                }
                if (pos == 2) {
                    obl.clear();
                    obl.add("Subjects");
                    listBySub();
                    ArrayAdapter<String> sadp = new ArrayAdapter<String>(this,
                            R.layout.support_simple_spinner_dropdown_item, obl);
                    ob.setAdapter(sadp);
                }
                else
                    Toast.makeText(this, "You have to chose sorting way", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ob:
                if (obl.get(0).equals("Names")){
                    op=pos;
                }
                if (obl.get(0).equals("Subjects")){
                    s= obl.get(pos);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * ListNames.
     * This function reads only the column names from a table and puts them in a list.
     */
    public void listByN(ArrayList obl){
        db=hlp.getReadableDatabase();
        String[] columns = {Students.NAME};
        String selection = Students.ACTIVE+"=?";
        String[] selectionArgs = {"1"};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        crsr=db.query(TABLE_STUDENTS, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        int col=crsr.getColumnIndex(Students.NAME);
        crsr.moveToFirst();
        while(!crsr.isAfterLast()){
            String n=crsr.getString(col);
            obl.add(n);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
    }
     public void listBySub(){
        db= hlp.getReadableDatabase();
        String [] columns= {Grades.SUBJECT};
        String selection= Grades.ACTIVE+"=?";
        String [] selectionArgs={"1"};
         String groupBy = null;
         String having = null;
         String orderBy = null;
         String limit = null;
         crsr=db.query(TABLE_GRADES, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
         int col=crsr.getColumnIndex(Grades.SUBJECT);
         crsr.moveToFirst();
         while(!crsr.isAfterLast()){
             String n=crsr.getString(col);
             if (!obl.contains(n))
                obl.add(n);
             crsr.moveToNext();
         }
         crsr.close();
         db.close();
     }

    public void ud(View view) {
        if (count==1)
            count=0;
        else
            count++;
    }

    /**
     * Sort.
     * @param view
     *
     * This function sends the final list to the ListView according to sorting
     * type(was chosen in req Spinner).
     */
    public void sort(View view) {
        fl.clear();
        if (obl.get(0).equals("Names")){
            db=hlp.getReadableDatabase();
            String[] columns = {Grades.MARK,Grades.SUBJECT,Grades.QUARTER,Grades.ID};
            String selection = Grades.ACTIVE+"=?";
            String[] selectionArgs = {"1"};
            String groupBy = null;
            String having = null;
            String orderBy;
            if (count==0)
                orderBy=Grades.MARK+" DESC";
            else
                orderBy=Grades.MARK+" ASC";
            String limit = null;
            crsr=db.query(TABLE_GRADES, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            int col1= crsr.getColumnIndex(Grades.MARK);
            int col2= crsr.getColumnIndex(Grades.SUBJECT);
            int col3= crsr.getColumnIndex(Grades.QUARTER);
            int col4= crsr.getColumnIndex(Grades.ID);
            crsr.moveToFirst();
            while(!crsr.isAfterLast()){
                int i= crsr.getInt(col4);
                if(i==op) {
                    String m = crsr.getString(col1);
                    String s1 = crsr.getString(col2);
                    String q1 = crsr.getString(col3);
                    String tmp = "mark: " + m + ", subject: " + s1 + ", quarter:" + q1 + ".";
                    fl.add(tmp);
                }
                crsr.moveToNext();
            }
            crsr.close();
            db.close();
            ArrayAdapter<String> fladp= new ArrayAdapter<String>(this,
                    R.layout.support_simple_spinner_dropdown_item,fl);
            flist.setAdapter(fladp);
        }
        else {
            listByN(ln);
            db=hlp.getReadableDatabase();
            String[] columns = {Grades.MARK,Grades.ID,Grades.QUARTER, Grades.SUBJECT};
            String selection = Grades.ACTIVE+"=?";
            String[] selectionArgs = {"1"};
            String groupBy = null;
            String having = null;
            String orderBy;
            if (count==0)
                orderBy=Grades.MARK+" DESC";
            else
                orderBy=Grades.MARK+" ASC";
            String limit = null;
            crsr=db.query(TABLE_GRADES, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            int col1= crsr.getColumnIndex(Grades.MARK);
            int col2= crsr.getColumnIndex(Grades.ID);
            int col3= crsr.getColumnIndex(Grades.QUARTER);
            int col4= crsr.getColumnIndex(Grades.SUBJECT);
            crsr.moveToFirst();
            while(!crsr.isAfterLast()){
                String sub= crsr.getString(col4);
                int i= crsr.getInt(col2);
                if(sub.equals(s)) {
                    String name = getname(i);
                    int m = crsr.getInt(col1);
                    int q = crsr.getInt(col3);
                    String tmp = "Name: " + name + ", mark:" + m + ", quarter:" + q + ".";
                    fl.add(tmp);
                }
                crsr.moveToNext();
            }
            crsr.close();
            db.close();
            ArrayAdapter<String> fladp= new ArrayAdapter<String>(this,
                    R.layout.support_simple_spinner_dropdown_item,fl);
            flist.setAdapter(fladp);
        }
    }

    /**
     * getname.
     *
     * This function searches names is STUDENT chart and adds them to the correct grades.
     */
    public String getname(int i){
        db=hlp.getWritableDatabase();
        String[] columns = {Students.NAME};
        String selection = Students.KEY_ID+"=?";
        String[] selectionArgs = {Integer.toString(i)};
        String groupBy = null;
        String having = null;
        String orderBy=null;
        String limit = null;
        crsr2=db.query(TABLE_STUDENTS, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        int col= crsr2.getColumnIndex(Students.NAME);
        crsr2.moveToNext();
        String s= crsr2.getString(col);
        crsr2.close();
        db.close();
        return s;
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
     * The item Sends a Toast to let the user know he is in the current page
     * he chose from the OptionMenu.
     */
    public void org(MenuItem item) {
        Toast.makeText(this, "You are already here :)", Toast.LENGTH_SHORT).show();

    }
    /**
     * Qg.
     *
     * @param item
     *
     * Moves user to Grades_info activity.
     */
    public void sad(MenuItem item) {
        Intent c= new Intent(this,ShowAndDelete.class);
        startActivity(c);
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