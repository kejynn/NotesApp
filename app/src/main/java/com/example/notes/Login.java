package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onRegister_click(View v)
    {
        startActivity(new Intent(this, Register.class));
    }

    public void onSignIn_click(View v)
    {
        Activity ctx = this;

        EditText txt_user = findViewById(R.id.LoginAuth);
        EditText txt_pass = findViewById(R.id.PassAuth);

        String args = "?name=" + txt_user.getText().toString() +
                "&secret=" + txt_pass.getText().toString();

        new ApiCall(this, "PUT", "session" + args)
        {
            public void on_ready(String result)
            {
                try {
                    JSONObject answer = new JSONObject(result);
                    if (answer.getString("status").equals("ok"))
                    {
                        String token = answer.getString("token");
                        Intent intent = new Intent(ctx, List.class);
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }
                    else {
                        on_fail();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            public void on_fail()
            {
                Toast toast = Toast.makeText(Login.this, "User doesn't exist", Toast.LENGTH_SHORT);
                toast.show();
            }
        };
    }

    public void onExit_click(View v)
    {
        finish();
    }
}