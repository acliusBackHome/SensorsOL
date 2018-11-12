package pw.assorted.servicetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent it;
    private SensorManager sm;
    private List<Sensor> ls;
    private SQLiteDatabase db;
    private Boolean count = false;
    private void logSensors(){
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        ls = sm.getSensorList(Sensor.TYPE_ALL);
        for(Sensor ss : ls){
            Log.d(ss.getName(),"hhh");
            db.execSQL("INSERT INTO sensors (item,value) VALUES (?,?);",new String[]{"SensorName",ss.getName()});
        }
    }
    private void initDB(){
        db = SQLiteDatabase.openOrCreateDatabase(getFilesDir()+"/my.db",null);
        db.execSQL("create table if not exists sensors(id integer primary key autoincrement,item text,value text)");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("onCreate", "create");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String logstr;
                if (count) {
                    logstr = "停止监视器";
                    stopService(it);
                } else {
                    logstr = "开始监视器";
                    startService(it);
                }
                count = !count;
                Snackbar.make(view, logstr, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        it = new Intent(MainActivity.this, MyService.class);

        Log.d("what","title");
        initDB();
        logSensors();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d("finalize","close");
        stopService(it);
        db.close();
        super.onDestroy();
    }
}
