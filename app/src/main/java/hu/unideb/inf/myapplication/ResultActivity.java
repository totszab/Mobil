package hu.unideb.inf.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView result;
    private Button vissza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        result = findViewById(R.id.finalResultTextView);
        vissza = findViewById(R.id.button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("Result");      //  pontszám
            result.setText(value);
        }
    }

    public void visszaButton(View view) {
        Intent replyIntent = new Intent();
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}