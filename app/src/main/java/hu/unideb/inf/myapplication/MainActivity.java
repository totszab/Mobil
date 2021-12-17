package hu.unideb.inf.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private CanvasView canvas;

    private final int circleRadius = 30;

    private float circleX;
    private float circleY;

    private float appleX;
    private float appleY;

    private float rottenX;
    private float rottenY;

    private int pont = 0;

    private int r_x_speed = 10;
    private int r_y_speed = 10;
    private int a_x_speed = 5;
    private int a_y_speed = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        circleX = getScreenWidth() / 2 - circleRadius;
        circleY = getScreenHeight() / 2 - circleRadius;

        almaPosition();
        rottenPosition();

        canvas = new CanvasView((MainActivity.this));
        setContentView(canvas);
    }

    public void openResultActivity(){
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);

        intent.putExtra("Result", pont+"");
        startActivity(intent);
    }

    ActivityResultLauncher addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    r_x_speed = 10;
                    r_y_speed = 10;
                    a_x_speed = 5;
                    a_y_speed = 5;
                    circleX = getScreenWidth() / 2 - circleRadius;
                    circleY = getScreenHeight() / 2 - circleRadius;
                    pont = 0;
                }
            });

    public void almaPosition()
    {
        appleX = getRandomNumber(circleRadius*2,getScreenWidth()-circleRadius*2);   // ne legyen teljesen kint
        appleY = getRandomNumber(circleRadius*2,getScreenHeight()-circleRadius*2);
    }
    public void rottenPosition()
    {
        rottenX = getRandomNumber(circleRadius*2,getScreenWidth()-circleRadius*2);
        rottenY = getRandomNumber(circleRadius*2,getScreenHeight()-circleRadius*2);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {

            float sensorX = sensorEvent.values[0];
            float sensorY = sensorEvent.values[1];

                float ujX = circleX - sensorX * 3;  //  sebesség
                float ujY = circleY + sensorY * 3;

            if (ujX < circleRadius)        //  zöld mozgatása
            {
                ujX = circleRadius;
            }

            if (ujY < circleRadius)
            {
                ujY = circleRadius;
            }

            if (ujX > getScreenWidth() - circleRadius)
            {
                ujX = getScreenWidth() - circleRadius;
            }

            if (ujY > getScreenHeight() - circleRadius)
            {
                ujY = getScreenHeight() - circleRadius ;
            }
/////////////////////////////////////////////////////// TODO
            rottenX += r_x_speed;
            rottenY  += r_y_speed;

            appleX += a_x_speed;
            appleY += a_y_speed;

            if (rottenX < circleRadius)        //  fekete mozgatása
            {
                r_x_speed = 10;
            }

            if (rottenY < circleRadius)
            {
                r_y_speed = 10;
            }

            if (rottenX > getScreenWidth() - circleRadius)
            {
                r_x_speed = -10;
            }

            if (rottenY > getScreenHeight() - circleRadius)
            {
                r_y_speed = -10;
            }
///////////////////////////////////////////////////////
            if (appleX < circleRadius)        //  piros mozgatása
            {
                a_x_speed = 5;
            }

            if (appleY < circleRadius)
            {
                a_y_speed = 5;
            }

            if (appleX > getScreenWidth() - circleRadius)
            {
                a_x_speed = -5;
            }

            if (appleY > getScreenHeight() - circleRadius )    //  -170 saját telefon
            {
                a_y_speed = -5;
            }
////////////////////////////////////////////////////////////
            circleX = Math.round(ujX);     //  új pozíció értéke
            circleY = Math.round(ujY);

            if ((circleX <= appleX + circleRadius  && circleX >= appleX || circleX >= appleX - circleRadius && circleX <= appleX) &&    // TODO: 30 is lehet elég
                    (circleY <= appleY + circleRadius && circleY >= appleY || circleY >= appleY - circleRadius) && circleY <= appleY )
            {
                pont++;
                almaPosition();
                rottenPosition();
            }

            if ((circleX <= rottenX + circleRadius  && circleX >= rottenX || circleX >= rottenX - circleRadius && circleX <= rottenX) &&    // TODO: 30 is lehet elég
                    (circleY <= rottenY + circleRadius && circleY >= rottenY || circleY >= rottenY - circleRadius) && circleY <= rottenY )
            {
                openResultActivity();
                r_x_speed = 0;
                r_y_speed = 0;
                a_x_speed = 0;
                a_y_speed = 0;
                almaPosition();
                rottenPosition();

            }

            canvas.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private class CanvasView extends View {
        private Paint apple;
        private Paint circle;
        private Paint result;
        private Paint rotten;

        public CanvasView(Context context){
            super(context);
            setFocusable(true);
            result = new Paint();
            apple = new Paint();
            circle = new Paint();
            rotten = new Paint();
        }

        public void onDraw(Canvas screen){

            result.setColor(Color.LTGRAY);
            result.setStyle(Paint.Style.FILL);
            result.setTextSize(300f);
            result.setAntiAlias(true);
            result.setAlpha(50);    //  opacity

            circle.setStyle(Paint.Style.FILL);
            circle.setAntiAlias(true);
            circle.setTextSize(30f);
            circle.setColor(Color.GREEN);

            apple.setStyle(Paint.Style.FILL);
            apple.setAntiAlias(true);
            apple.setTextSize(30f);
            apple.setColor(Color.RED);

            rotten.setStyle(Paint.Style.FILL);
            rotten.setAntiAlias(true);
            rotten.setTextSize(30f);
            rotten.setColor(Color.BLACK);


            screen.drawText(pont+"", getScreenWidth()/2 - 75, getScreenHeight()/2 , result);
            screen.drawCircle(rottenX,rottenY,circleRadius,rotten);
            screen.drawCircle(appleX,appleY,circleRadius,apple);
            screen.drawCircle(circleX,circleY,circleRadius, circle);

        }
    }
}