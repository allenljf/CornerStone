package com.trubuzz.cornerstone.widget.codePicker;

import org.json.JSONException;

import java.io.IOException;


public abstract class ExceptionCallback {
    public abstract void onIOException(IOException e);
    public abstract void onJSONException(JSONException e);
}
