package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class List extends AppCompatActivity {

    String token;
    ArrayAdapter <NoteEntity> adapter;

    void update()
    {
        adapter.clear();
        String args = "?token=" + token;
        new ApiCall(this, "GET", "note" + args)
        {
            public void on_ready(String result)
            {
                try
                {
                    JSONArray items = new JSONArray(result);
                    for (int i = 0; i < items.length(); i++)
                        adapter.add(new NoteEntity(items.getJSONObject(i)));
                    adapter.notifyDataSetChanged();
                }
                catch (JSONException e) {}
            }

            public void on_fail()
            {
                Toast t = Toast.makeText(List.this, "Токен не существует", Toast.LENGTH_SHORT);
                t.show();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Activity ctx = this;

        adapter = new ArrayAdapter<NoteEntity>(this, android.R.layout.simple_list_item_1);
        ListView list = findViewById(R.id.NoteList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                NoteEntity note = adapter.getItem(i);
                Intent intent = new Intent(ctx, Note.class);
                intent.putExtra("token", token);
                intent.putExtra("note_id", note.id);
                intent.putExtra("note_title", note.title);
                startActivityForResult(intent,0);
            }
        });

        token = getIntent().getStringExtra("token");
        update();
    }

    public  void onSignOut_click(View v)
    {
        String args = "?token=" + token;
        new ApiCall(this, "DELETE", "session" + args)
        {
            public void on_ready(String result)
            {
                try {
                    JSONObject answer = new JSONObject(result);
                    if (answer.getString("status").equals("ok"))
                    {
                        finish();
                        Toast t = Toast.makeText(List.this, "Сессия завершена", Toast.LENGTH_SHORT);
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
                Toast t = Toast.makeText(List.this, "Токен не существует", Toast.LENGTH_SHORT);
                t.show();
            }
        };
    }

    public  void onNew_click(View v)
    {
        String args = "?token=" + token + "&title=untitled&content=content";
        new ApiCall(this, "PUT", "note" + args)
        {
            public void on_ready(String result)
            {
                try {
                    JSONObject answer = new JSONObject(result);
                    if (answer.getString("status").equals("ok"))
                    {
                        Toast t = Toast.makeText(List.this, "Новая заметка добавлена", Toast.LENGTH_SHORT);
                        t.show();
                        update();
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
                Toast t = Toast.makeText(List.this, "Токен не существует", Toast.LENGTH_SHORT);
                t.show();
            }
        };
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        update();
        super.onActivityResult(requestCode, resultCode, data);
    }
}