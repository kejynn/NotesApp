package com.example.notes;

import org.json.JSONException;
import org.json.JSONObject;

public class NoteEntity
{
    public int id;
    public String title;

    public NoteEntity(JSONObject obj) throws JSONException
    {
        id = obj.getInt("id");
        title = obj.getString("title");
    }


    public String toString() {return "     " +  id  + "                                             "+ title;}
}
