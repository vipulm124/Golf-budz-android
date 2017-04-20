package com.adcoretechnologies.golfbudz.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gaurav
 */
public class Validations {
    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    /*public static boolean isValidPassword(String pass) {
		if (pass != null && pass.length() > 6) {
			return true;
		}
		return false;
	}*/

    // validating username
    public static boolean isValidUsername(String username) {
        String USERNAME_PATTERN = "^[a-z_-]{3,15}$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        String PASSWORD_PATTERN = "^[a-zA-Z0-9. ]{6,25}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // validating username
    public static boolean isValidMobile(String username) {
        String USERNAME_PATTERN = "[7-9][0-9]{9}$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isFieldEmpty(EditText ed) {
        if (ed != null) {
            String uname = ed.getText().toString().trim();
            if (uname.equals("") || uname.length() <= 0)
                return true;
        }
        return false;
    }
}
