package ir.university.toosi.wtms.web.util;

import java.util.Locale;


/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class LangUtils {
    public static Locale LOCALE_FARSI = new Locale("fa");
    public static Locale LOCALE_ENGLISH = Locale.ENGLISH;


    public static Locale refine(Locale l) {

        if (l == null)
            return LOCALE_ENGLISH;

        if ("fa".equalsIgnoreCase(l.getLanguage()))
            return LOCALE_FARSI;

        return LOCALE_ENGLISH;

    }

    public static String getEnglishNumber(String number) {
        StringBuffer farsiNumberString = new StringBuffer();
        char c;

        for (int i = 0; i < number.length(); i++) {
            c = number.charAt(i);
            switch (c) {
                case '?':
                    farsiNumberString.append('0');
                    break;

                case '?':
                    farsiNumberString.append('1');
                    break;

                case '?':
                    farsiNumberString.append('2');
                    break;

                case '?':
                    farsiNumberString.append('3');
                    break;

                case '?':
                    farsiNumberString.append('4');
                    break;

                case '?':
                    farsiNumberString.append('5');
                    break;

                case '?':
                    farsiNumberString.append('6');
                    break;

                case '?':
                    farsiNumberString.append('7');
                    break;

                case '?':
                    farsiNumberString.append('8');
                    break;

                case '?':
                    farsiNumberString.append('9');
                    break;

                default:
                    farsiNumberString.append(c);
                    break;
            }
        }
        return farsiNumberString.toString();
    }

}