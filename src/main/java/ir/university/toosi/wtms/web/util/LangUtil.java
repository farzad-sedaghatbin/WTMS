package ir.university.toosi.wtms.web.util;

import java.util.Locale;

public class LangUtil {
    public static Locale LOCALE_FARSI = new Locale("fa");
    public static Locale LOCALE_ENGLISH = Locale.ENGLISH;

    public static String getNumber(int number, Locale locale) {
        return getNumber("" + number, locale);
    }

    public static String getNumber(String number, Locale locale) {
        if (locale.equals(LOCALE_FARSI))
            return getFarsiNumber(number);
        return number;
    }

    public static String getFarsiNumber(String number) {
        StringBuffer farsiNumberString = new StringBuffer();
        char c;

        for (int i = 0; i < number.length(); i++) {
            c = number.charAt(i);
            switch (c) {
                case '0':
                    farsiNumberString.append('?');
                    break;

                case '1':
                    farsiNumberString.append('?');
                    break;

                case '2':
                    farsiNumberString.append('?');
                    break;

                case '3':
                    farsiNumberString.append('?');
                    break;

                case '4':
                    farsiNumberString.append('?');
                    break;

                case '5':
                    farsiNumberString.append('?');
                    break;

                case '6':
                    farsiNumberString.append('?');
                    break;

                case '7':
                    farsiNumberString.append('?');
                    break;

                case '8':
                    farsiNumberString.append('?');
                    break;

                case '9':
                    farsiNumberString.append('?');
                    break;

                default:
                    farsiNumberString.append(c);
                    break;
            }
        }
        return farsiNumberString.toString();
    }

    public static String getEnglishNumber(String number) {
        StringBuffer englishNumberString = new StringBuffer();
        char c;

        for (int i = 0; i < number.length(); i++) {
            c = number.charAt(i);
            switch (c) {
                /*case '?':
                    englishNumberString.append('0');
                    break;

                case '?':
                    englishNumberString.append('1');
                    break;

                case '?':
                    englishNumberString.append('2');
                    break;

                case '?':
                    englishNumberString.append('3');
                    break;

                case '?':
                    englishNumberString.append('4');
                    break;

                case '?':
                    englishNumberString.append('5');
                    break;

                case '?':
                    englishNumberString.append('6');
                    break;

                case '?':
                    englishNumberString.append('7');
                    break;

                case '?':
                    englishNumberString.append('8');
                    break;

                case '?':
                    englishNumberString.append('9');
                    break;
*/
                default:
                    englishNumberString.append(c);
                    break;
            }
        }
        return englishNumberString.toString();
    }

    public static Locale refine(Locale l) {
        if (l == null)
            return LOCALE_FARSI;
        if ("fa".equalsIgnoreCase(l.getLanguage()))
            return LOCALE_FARSI;

        return LOCALE_FARSI;
    }
}