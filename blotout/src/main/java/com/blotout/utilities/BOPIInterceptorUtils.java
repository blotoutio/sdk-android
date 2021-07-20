package com.blotout.utilities;

import android.telephony.PhoneNumberUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BOPIInterceptorUtils {
    private static final String TAG = "BOPIInterceptorUtils";

    //Card validation
    public static final byte VISA = 0;
    public static final byte MASTERCARD = 1;
    public static final byte AMEX = 2;
    public static final byte DINERS_CLUB = 3;
    public static final byte CARTE_BLANCHE = 4;
    public static final byte DISCOVER = 5;
    public static final byte ENROUTE = 6;
    public static final byte JCB = 7;

    public static boolean isValidEmail(@Nullable CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidEmail(@Nullable String target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPhoneNumber(String targetString) {
        return PhoneNumberUtils.isGlobalPhoneNumber(targetString);

    }

    public static boolean validate(@NonNull final String credCardNumber, final byte type) {
        String creditCard = credCardNumber.trim();
        boolean applyAlgo = false;
        switch (type) {
            case VISA:
                // VISA credit cards has length 13 - 15
                // VISA credit cards starts with prefix 4
                if (creditCard.length() >= 13 && creditCard.length() <= 16
                        && creditCard.startsWith("4")) {
                    applyAlgo = true;
                }
                break;
            case MASTERCARD:
                // MASTERCARD has length 16
                // MASTER card starts with 51, 52, 53, 54 or 55
                if (creditCard.length() == 16) {
                    int prefix = Integer.parseInt(creditCard.substring(0, 2));
                    if (prefix >= 51 && prefix <= 55) {
                        applyAlgo = true;
                    }
                }
                break;
            case AMEX:
                // AMEX has length 15
                // AMEX has prefix 34 or 37
                if (creditCard.length() == 15
                        && (creditCard.startsWith("34") || creditCard
                        .startsWith("37"))) {
                    applyAlgo = true;
                }
                break;
            case DINERS_CLUB:
            case CARTE_BLANCHE:
                // DINERSCLUB or CARTEBLANCHE has length 14
                // DINERSCLUB or CARTEBLANCHE has prefix 300, 301, 302, 303, 304,
                // 305 36 or 38
                if (creditCard.length() == 14) {
                    int prefix = Integer.parseInt(creditCard.substring(0, 3));
                    if ((prefix >= 300 && prefix <= 305)
                            || creditCard.startsWith("36")
                            || creditCard.startsWith("38")) {
                        applyAlgo = true;
                    }
                }
                break;
            case DISCOVER:
                // DISCOVER card has length of 16
                // DISCOVER card starts with 6011
                if (creditCard.length() == 16 && creditCard.startsWith("6011")) {
                    applyAlgo = true;
                }
                break;
            case ENROUTE:
                // ENROUTE card has length of 16
                // ENROUTE card starts with 2014 or 2149
                if (creditCard.length() == 16
                        && (creditCard.startsWith("2014") || creditCard
                        .startsWith("2149"))) {
                    applyAlgo = true;
                }
                break;
            case JCB:
                // JCB card has length of 16 or 15
                // JCB card with length 16 starts with 3
                // JCB card with length 15 starts with 2131 or 1800
                if ((creditCard.length() == 16 && creditCard.startsWith("3"))
                        || (creditCard.length() == 15 && (creditCard
                        .startsWith("2131") || creditCard
                        .startsWith("1800")))) {
                    applyAlgo = true;
                }
                break;
            default:
                //throw new IllegalArgumentException();
        }
        if (applyAlgo) {
            return validate(creditCard);
        }
        return false;
    }

    private static boolean validate(@NonNull String creditCard) {
        int sum = 0;
        int length = creditCard.length();
        for (int i = 0; i < creditCard.length(); i++) {
            if (0 == (i % 2)) {
                sum += creditCard.charAt(length - i - 1) - '0';
            } else {
                sum += sumDigits((creditCard.charAt(length - i - 1) - '0') * 2);
            }
        }
        return 0 == (sum % 10);
    }

    private static int sumDigits(int i) {
        return (i % 10) + (i / 10);
    }


    public static boolean validateCreditCardNumber(@NonNull String str) {
        //Here I am providing java Luhn Algorithm program to validate credit card numbers
        //https://www.journaldev.com/1443/java-credit-card-validation-luhn-algorithm-java

        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 == 0) {
            Logger.INSTANCE.v(TAG,str + " is a valid credit card number");
            return true;

        } else {
            Logger.INSTANCE.v(TAG,str + " is an invalid credit card number");
        }

        return false;
    }

    public static boolean isValidSSN(String target) {


        String regex = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";

        Pattern pattern = Pattern.compile(regex);


        Matcher matcher = pattern.matcher(target);
        return matcher.matches();

    }
    public static boolean validateAadharNumber(@NonNull String aadharNumber){
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    public static boolean isUSZipCode(String target) {

        String regex = "^d{5}(-d{4})?$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();

    }


    public static boolean isUSZipCodeValidation(String target) {
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$";

        Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(target);
            return matcher.matches();
    }

    public static boolean isZipCode(String target) {

        String regex = " ^[1-9][0-9]{5}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();

    }


}
