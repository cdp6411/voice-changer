package voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils;


import java.util.regex.Pattern;

public class StringUtils {
    public static final String REGEX_SPECIAL_CHARACTER = "[^a-zA-Z0-9_]";
    public static final String TAG = "StringUtils";


    public static boolean isContainsSpecialCharacter(String str) {
        return (str == null || str.equals("") || !Pattern.compile(REGEX_SPECIAL_CHARACTER).matcher(str).find()) ? false : true;
    }

    public static boolean isEmptyString(String str) {
        return str == null || str.equals("");
    }


}
