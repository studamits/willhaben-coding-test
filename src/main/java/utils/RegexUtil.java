package utils;

public class RegexUtil {

    public static final String FROM_EMAIL_REGEX = "^From: [a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    public static final String TO_EMAIL_REGEX = "^To: [a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    public static final String STAMP_REGEX = "^Stamp: [0-9A-Za-z+/=]+$";

    public static final String NONCE_REGEX = "^Nonce: [0-9]+$";
}
