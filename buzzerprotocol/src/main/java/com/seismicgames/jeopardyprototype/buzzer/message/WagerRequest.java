package com.seismicgames.jeopardyprototype.buzzer.message;

import java.util.List;

/**
 * Created by jduffy on 6/28/16.
 */
public class WagerRequest extends BuzzerMessage {
    public int wager;

    public WagerRequest(int wager) {
        this.wager = wager;
    }
}
