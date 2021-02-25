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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.ex026.Students.TABLE_STUDENTS;

/**
 * The type Filling grades.
 *
 * @author Tahel Hazan <th8887@bs.amalnet.k12.il>
 * @version 1.1.6
 * @since 11.12.2020  This activity is part 2 of entering information- now the user enters grades, subjects and quarters.
 */
public class Filling_Grades extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ContentValues cv= new ContentValues();
    String[] quarters={"Quarter","1","2","3","4"};
    /**
     * The Quar.
     * @param m- for grades;
     * @param sub- for subject.
     */
    Spinner quar, ident;
    EditText m, sub;
    /**
     * The Names.
     * ArrayList for the names.
     */
    ArrayList<String> names=new ArrayList<>();
    /**
     * The S.
     *
     * @param s - subject.
     * @param g - grade from EditText.
     * @param d - id for Grade Chart.
     */
    String s, g;
    /**
     *
     * @param gn - grade casted to int.
     */
    int gn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling__grades);

        quar=(Spinner) findViewById(R.id.quarter);
        m=(EditText) findViewById(R.id.m);
        sub=(EditText) findViewById(R.id.sub);
        ident=(Spinner) findViewById(R.id.ident);

        hlp= new HelperDB(this);
        db=hlp.getWritableDatabase();
        db.close();

        ident.setOnItemSelectedListener(this);
        quar.setOnItemSelectedListener(this);

        ArrayAdapter<String> adp= new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, quarters);
        quar.setAdapter(adp);
        names.add("Names");
        listNames();

        ArrayAdapter<String> adpN= new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, names);
        ident.setAdapter(adpN);
    }

    /**
     * List names.
     *
     * Creates list of names from table STUDENTS.
     */
    public void listNames(){
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
            names.add(n);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
    }

    /**
     * Submitg.
     *
     * @param view the view
     *
     * Takes the information from the EditTexts to the GRADES table.
     */
    public void submitg(View view) {
        if(sub.getText().toString().equals("")||m.getText().toString().equals("")){
            Toast.makeText(this, "enter information according to the orders.", Toast.LENGTH_SHORT).show();
        }
        else{
            s=sub.getText().toString();
            g=m.getText().toString();
            gn=Integer.parseInt(g);
            if(!(gn>=0)&&!(gn<=100)){
                Toast.makeText(this, "The grade supposed to be between 0 to 100", Toast.LENGTH_SHORT).show();
            }
            else{
                cv.put(Grades.MARK,gn);
                cv.put(Grades.SUBJECT,s);
            }
        }
        cv.put(Grades.ACTIVE,1);
        db = hlp.getWritableDatabase();
        db.insert(Grades.TABLE_GRADES, null, cv);
        db.close();
        m.setText("");
        sub.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch (parent.getId()){
        case R.id.quarter:cv.put(Grades.QUARTER,pos);break;
        case R.id.ident:cv.put(Grades.ID,pos);break;
    }
    }

    @Override
    public void onNothingSelected(AdapterView<?> par) {
    }

    /**
     * Fin.
     *
     * @param item Moves the user to Main Activity(filling personal information) activity.
     */
    public void fin(MenuItem item) {
        Intent c= new Intent(this,MainActivity.class);
        startActivity(c);
    }

    /**
     * Fing.
     *
     * @param item The item Sends a Toast to let the user know he is in the current page he chose from the OptionMenu.
     */
    public void fing(MenuItem item) {
        Toast.makeText(this, "You are already here :)", Toast.LENGTH_SHORT).show();
    }

    /**
     * Pn.
     *
     * @param item Moves user Personal info activity.
     */
    public void org(MenuItem item) {
        Intent c= new Intent(this,Organize.class);
        startActivity(c);
    }

    /**
     * Qg.
     *
     * @param item Moves user to Grades_info activity.
     */
    public void sad(MenuItem item) {
        Intent c= new Intent(this,ShowAndDelete.class);
        startActivity(c);
    }

    /**
     * Cred.
     *
     * @param item Moves the user the Credits activity.
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