package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class Note extends AppCompatActivity {

    int id;
    String token;
    EditText txt_title;
    EditText txt_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        txt_title = findViewById(R.id.EnterTitle);
        txt_context = findViewById(R.id.EnterContext);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        id = intent.getIntExtra("note_id", -1);
        txt_title.setText(intent.getStringExtra("note_title"));

        String args = "?token=" + token + "&id=" + String.valueOf(id);
        new ApiCall(this, "GET", "note" + args)
        {
            public void on_ready(String result)
            {
                try {
                    JSONObject answer = new JSONObject(result);
                    if (answer.getString("status").equals("ok"))
                    {
                        String content = answer.getString("Content");
                        txt_context.setText(content);
                    }
                    else
                    {
                        on_fail();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            public void on_fail()
            {
                Toast t = Toast.makeText(Note.this, "Note doesnt exist", Toast.LENGTH_SHORT);
                t.show();
            }
        };
    }
    public void onClose_click(View v)
    {
        Log.e("tag", "test");
        finish();
    }

    public void onSave_click(View v)
    {
        String title = URLEncoder.encode(txt_title.getText().toString());
        String context = URLEncoder.encode(txt_context.getText().toString());

        String args = "?token=" + token + "&id=" + String.valueOf(id) +
                "&title=" + title + "&content=" + context;
        new ApiCall(this, "POST", "note" + args)
        {
            public void on_ready(String result)
            {
                try {
                    JSONObject answer = new JSONObject(result);
                    if (answer.getString("status").equals("ok"))
                    {
                        finish();
                        Toast t = Toast.makeText(Note.this, "Заметка сохранена", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    else
                    {
                        on_fail();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            public void on_fail()
            {
                Toast t = Toast.makeText(Note.this, "Заметка не существует", Toast.LENGTH_SHORT);
                t.show();
            }
        };
    }
    public void onDelete_click(View v)
    {
        String args = "?token=" + token + "&id=" + String.valueOf(id);
        new ApiCall(this, "DELETE", "note"+args)
        {
            public void on_ready(String result)
            {
                try {
                    JSONObject answer = new JSONObject(result);
                    if (answer.getString("status").equals("ok"))
                    {
                        finish();
                        Toast t = Toast.makeText(Note.this, "Заметка удалена", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    else
                    {
                        on_fail();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            public void on_fail()
            {
                Toast t = Toast.makeText(Note.this, "Заметка не существует", Toast.LENGTH_SHORT);
                t.show();
            }
        };
    }
}