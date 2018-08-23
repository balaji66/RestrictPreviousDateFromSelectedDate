package com.durga.balaji66.restrictpreviousdatefromselecteddate;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  implements View.OnTouchListener{

    private EditText mFromDate, mToDate;
    private DatePickerDialog mDatePickerDialog;
    private int mYearFrom, mMonthFrom, mDayFrom;
    static boolean b = false;
    private String fromdate,todate;
    boolean isOkayClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeListeners();
        mToDate.setEnabled(false);
    }


    public void initializeViews()
    {
        mFromDate =(EditText)findViewById(R.id.editTextFromDate);
        mToDate =(EditText)findViewById(R.id.editTextToDate);
    }
    public void initializeListeners()
    {
        mFromDate.setOnTouchListener(this);
        mToDate.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        final int DRAWABLE_RIGHT = 2;
        switch (v.getId())
        {
            case R.id.editTextFromDate:
                if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if (motionEvent.getRawX() >= (mFromDate.getRight() - mFromDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Calendar calendar = Calendar.getInstance();
                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                        mYearFrom =mYear;
                        mMonthFrom =mMonth;
                        mDayFrom =mDay;

                        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                            // when dialog box is closed, below method will be called.
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                if (isOkayClicked) {
                                    mFromDate.setText(selectedYear +"-"+ (selectedMonth + 1)+ "-" + selectedDay);
                                    mYearFrom = selectedYear;
                                    mMonthFrom = selectedMonth;
                                    mDayFrom = selectedDay;
                                    fromdate=String.valueOf(mFromDate.getText().toString());
                                    mToDate.setEnabled(true);
                                }
                                isOkayClicked = false;
                            }
                        };
                        final DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, datePickerListener,
                                mYearFrom, mMonthFrom, mDayFrom);

                        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                                "Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                                            dialog.cancel();
                                            isOkayClicked = false;
                                        }
                                    }
                                });

                        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                "OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        if (which == DialogInterface.BUTTON_POSITIVE) {
                                            isOkayClicked = true;
                                            DatePicker datePicker = datePickerDialog
                                                    .getDatePicker();
                                            datePickerListener.onDateSet(datePicker,
                                                    datePicker.getYear(),
                                                    datePicker.getMonth(),
                                                    datePicker.getDayOfMonth());
                                        }
                                    }
                                });
                        datePickerDialog.setCancelable(false);
                        datePickerDialog.show();

                    }
                }

            case R.id.editTextToDate:
                if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if (motionEvent.getRawX() >= mToDate.getRight() - mToDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {
                        Calendar calendar = Calendar.getInstance();
                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        final int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                        mDatePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                mToDate.setText( year+ "-" + (month + 1) + "-" + day);
                                todate=String.valueOf(mToDate.getText().toString());
                                CheckDates(fromdate,todate);
                            }
                        }, mYear, mMonth, mDay);
                        mDatePickerDialog.show();
                    }
                }
                return true;
        }
        return true;
    }


    /**
     * Creating object for simple date format
     */
    static SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");

    public boolean CheckDates(String d1, String d2)    {

        try {
            /**
             * If start date is after the end date
             * return true
             */
            if(dfDate.parse(d1).before(dfDate.parse(d2)))
            {
                b = true;
            }
            /**
             * If start date is before end date
             * return true
             */
            else if(dfDate.parse(d1).after(dfDate.parse(d2)))
            {
                b = true;
                mToDate.setText("");
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setMessage("You can't select past date from selected date!")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
            /**
             * If two dates are equal
             * return true
             */
            else
            {
                b = false;
                // mToDateTextInputEditText.setText("");
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

}
