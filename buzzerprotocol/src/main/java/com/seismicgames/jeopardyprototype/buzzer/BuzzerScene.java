package com.seismicgames.jeopardyprototype.buzzer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jduffy on 7/21/16.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BuzzerScene {
    enum Scene {
        REMOTE, BUZZER;

        public static Scene parse(String str) {
            for (Scene s : values()) {
                if (s.name().equals(str)) return s;
            }
            return null;
        }
    }

    Scene value();
}
