package pw.assorted.servicetest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.net.DatagramSocket;
import java.util.Date;
import java.util.Vector;

public class MyService extends Service implements SensorEventListener{
    private SensorManager sm;
    private static final String serverHost = "192.168.1.102";
    private static final int serverPort = 8141;

    private InetAddress serverAddr;
    private DatagramSocket socket;
    private Thread socketTh;

    class UDPInit implements Runnable {
        @Override
        public void run() {
            try {
                serverAddr = InetAddress.getByName(serverHost);
                socket = new DatagramSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class UDPSend implements Runnable {
        private String json;
        UDPSend(String txt){
            json=txt;
        }
        public void run() {
            byte[] data = json.getBytes();
            try {
                Log.d(json, "trying sending:" + json);
                socket.connect(serverAddr,serverPort);
                DatagramPacket pckt = new DatagramPacket(data, data.length, serverAddr, serverPort);
                socket.send(pckt);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void onCreate() {
        super.onCreate();
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SE); // 加速度计
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL); // 磁力计
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL); //  方向传感器
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ALL), SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL); // 光传感器
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL); // 旋转矢量传感器
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_NORMAL); // 重力传感器
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL); // 陀螺仪传感器
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL); // 线性加速度传感器
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL); // 近距离传感器
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_NORMAL); // 压力传感器
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_NORMAL); // 温度传感器
        Toast.makeText(getApplicationContext(), "服务创建", Toast.LENGTH_LONG).show();
        socketTh=new Thread(new UDPInit());
        socketTh.start();
    }
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "another Toast", Toast.LENGTH_LONG).show();
        return null;
    }
    public void onDestory(){
        Log.d("on Destory","destory");
        Toast.makeText(getApplicationContext(), "服务销毁", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
    private String covertJSON(String name,float[] vs){
        char s='x';
        long t=new Date().getTime();
        String ret="{\"item\": \"" + name +"\",\"data\": [";
        ret += "{\"name\": \"time\",\"value\": \"" + String.valueOf(t) + "\"}";
        for(int i=0;i<vs.length;i++){
            ret+=String.format(",{\"name\": \"%c\",\"value\": \"%f\"}",(char)(s+i),vs[i]);
        }
        ret+="]"+",\"time\": \"" + String.valueOf(t)+"\"}";
        Log.d("json",ret);
        return ret;
    }
    @Override
    public void onAccuracyChanged(Sensor s,int a){
        Log.d(s.getName(),"onAccuracy");
        Log.d(String.format("%d",a),"int");
    }
    @Override
    public void onSensorChanged(SensorEvent se){
        {
            if(se.sensor.getType()==Sensor.TYPE_ACCELEROMETER){ // 1
                Log.d(String.format("[x,y,z]=[%f,%f,%f]",se.values[0],se.values[1],se.values[2]),"加速度");
            }
            if(se.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){ // 2
                Log.d(String.format("[x,y,z]=[%f,%f,%f]",se.values[0],se.values[1],se.values[2]),"磁场强度");
            }
            if(se.sensor.getType()==Sensor.TYPE_ORIENTATION){ // 3
                Log.d(String.format("[x,y,z]=[%f,%f,%f]",se.values[0],se.values[1],se.values[2]),"方向传感器");
            }
            if(se.sensor.getType()==Sensor.TYPE_GYROSCOPE){ // 4
                Log.d(String.format("[x,y,z]=[%f,%f,%f]",se.values[0],se.values[1],se.values[2]),"陀螺仪传感器");
            }
            if(se.sensor.getType()==Sensor.TYPE_GRAVITY){ // 5
                Log.d(String.format("[x,y,z]=[%f,%f,%f]",se.values[0],se.values[1],se.values[2]),"重力传感器");
            }
            if(se.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){ // 6
                Log.d(String.format("[x,y,z]=[%f,%f,%f]",se.values[0],se.values[1],se.values[2]),"线性加速度");
            }
            if(se.sensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE){ // 7
                Log.d("我有温度传感器！","温度传感器");
            }
            if(se.sensor.getType()==Sensor.TYPE_LIGHT){ // 8
                Log.d(String.format("光强:%f",se.values[0]),"光传感器");
            }
            if(se.sensor.getType()==Sensor.TYPE_PROXIMITY){ // 9
                Log.d(String.format("距离:%f",se.values[0]),"距离传感器");
            }
            if(se.sensor.getType()==Sensor.TYPE_PRESSURE){ // 10
                Log.d("我有压力传感器!","压力传感器");
            }
            if(se.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){ // 11
                Log.d(String.format("[x,y,z]=[%f,%f,%f]",se.values[0],se.values[1],se.values[2]),"旋转矢量");
            }
        }
        new Thread(new UDPSend(covertJSON(se.sensor.getName(),se.values))).start();
    }
}
