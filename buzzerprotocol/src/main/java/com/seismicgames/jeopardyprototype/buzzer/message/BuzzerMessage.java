package com.seismicgames.jeopardyprototype.buzzer.message;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by jduffy on 6/29/16.
 */
public abstract class BuzzerMessage {
    public String type = getClass().getSimpleName();

}
