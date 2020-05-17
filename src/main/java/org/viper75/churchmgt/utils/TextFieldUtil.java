package org.viper75.churchmgt.utils;

import javafx.scene.control.TextField;

public class TextFieldUtil {
    public static void addTextLimiter(final TextField tf, final int maxLength){
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength){
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }

    public static void addTextLimiter(final TextField[] textFields, final int maxLength){
        for (TextField tf : textFields){
            tf.textProperty().addListener((observable, oldValue, newValue) -> {
                if (tf.getText().length() > maxLength){
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            });
        }
    }
}
