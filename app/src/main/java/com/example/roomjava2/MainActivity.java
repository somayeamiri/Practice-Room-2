package com.example.roomjava2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements UnzipListener {

    public static final String TAG = "Roomjava2";
    String DATABASE_NAME = "endb.db";
    String FILE = "/data/data/com.example.roomjava2/databases/";

    EditText word;
    Button btn;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        word=findViewById(R.id.edt_word);
        btn=findViewById(R.id.btn);
        show=findViewById(R.id.edt_show);

        //check if database doesnt exist in storage
        File file = new File(FILE + DATABASE_NAME);
        if (!file.exists()) {

            //download database using asynctask and show progressbar
            DownloadDataBase downloadDataBase = new DownloadDataBase(MainActivity.this, "dic.zip", MainActivity.this);
            downloadDataBase.execute("http://195.248.242.73/androidteam/p1/database.zip", "/data/data/com.example.roomjava2/databases/");

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //query to database
                connectDatabasAndQuery(word.getText().toString());


            }
        });




    }

    void connectDatabasAndQuery(final String sample) {

        new Thread(new Runnable() {
            @Override
            public void run() {


                final Dic dic = WordRoomDatabase.getDatabase(MainActivity.this, DATABASE_NAME).wordDao().GetWord(sample);
                if (dic != null) {

                    Log.i(TAG, dic.translate);

                    //toast from thread
                    toastOnUi(dic.translate);

                } else {

                    Log.i(TAG, "not found");

                    toastOnUi("not found");

                }


            }
        }).start();

    }

    @Override
    public void unzipFinished() {
//        connectDatabasAndQuery();

        Toast.makeText(this, "unzipping finished", Toast.LENGTH_SHORT).show();
    }

    private void toastOnUi(final String message){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {

//                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                show.setText(message);

            }
        });

    }
}