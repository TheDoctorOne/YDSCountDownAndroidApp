package com.carefill.ydscountdown2019;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView remainingTime;
    TextView selectedTimeOption;

    public static boolean SEC = true;
    public static boolean HOUR = false;
    public static boolean DAY = false;
    public static final String EXAM_DATE = "08.09.2019 10:15:00"; //dd.MM.yyyy HH:mm:ss
    public long millisToExamDate;



    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what > 0) {
                CharSequence cs = msg.what + "";
                remainingTime.setText(cs);
            } else {
                CharSequence cs = "Sınav Oldu";
                remainingTime.setText(cs);
                selectedTimeOption.setText("");
            }
            return false;
        }
    });



    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByIds();
        try {
            millisToExamDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(EXAM_DATE).getTime();
        } catch (ParseException e) {
            Toast.makeText(this,"Tarih hesaplanamadi.", Toast.LENGTH_SHORT).show();
            System.exit(1);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long curTime = Calendar.getInstance().getTimeInMillis();
                    long difference = millisToExamDate - curTime;
                    if(SEC) {
                        int remainSec = (int) (difference / 1000);
                        Message message = handler.obtainMessage();
                        message.what = remainSec;
                        message.sendToTarget();
                    } else if (HOUR) {
                        int remainHour = (int) (difference / 1000 / 60 / 60); //Saniye / Dakika / Saat
                        Message message = handler.obtainMessage();
                        message.what = remainHour;
                        message.sendToTarget();
                    } else if (DAY) {
                        int remainDay = (int) (difference / 1000 / 60 / 60 / 24); //Saniye / Dakika / Saat / Gün
                        Message message = handler.obtainMessage();
                        message.what = remainDay;
                        message.sendToTarget();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void findViewByIds() {
        remainingTime = findViewById(R.id.remainingText);
        selectedTimeOption = findViewById(R.id.selectedTimeOption);
    }

    @SuppressLint("SetTextI18n")
    public void secOnClick(View view) {
        selectedTimeOption.setText("Saniye");
        SEC = true;
        HOUR = false;
        DAY = false;
    }
    @SuppressLint("SetTextI18n")
    public void hourOnClick(View view) {
        selectedTimeOption.setText("Saat");
        SEC = false;
        HOUR = true;
        DAY = false;
    }
    public void daysOnClick(View view) {
        selectedTimeOption.setText("Gün");
        SEC = false;
        HOUR = false;
        DAY = true;
    }
}
