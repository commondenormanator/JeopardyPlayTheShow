package com.seismicgames.jeopardyprototype;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by jduffy on 7/26/16.
 */
public class NumberUtil {

    public static final String[] DIGITS = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "zero", "oh"};
    public static final String[] TENS = {"ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
    public static final String[] TEENS = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};

    public static final String[] MAGNITUDES = {"hundred", "thousand", "million"};

    public static final int[] MAG_VALS = {100, 1000, 1000000};
    public static final int[] TEN_VALS = {10, 20,30,40,50,60,70,80,90};
    public static final int[] TEEN_VALS = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    public static final int[] DIGIT_VALS = {1,2,3,4,5,6,7,8,9,0,0};

//    public static String replaceNumbers (String input) {
//        String result = "";
//        String[] decimal = input.split(MAGNITUDES[3]);
//        String[] millions = decimal[0].split(MAGNITUDES[2]);
//
//        for (int i = 0; i < millions.length; i++) {
//            String[] thousands = millions[i].split(MAGNITUDES[1]);
//
//            for (int j = 0; j < thousands.length; j++) {
//                int[] triplet = {0, 0, 0};
//                StringTokenizer set = new StringTokenizer(thousands[j]);
//
//                if (set.countTokens() == 1) { //If there is only one token given in triplet
//                    String uno = set.nextToken();
//                    triplet[0] = 0;
//                    for (int k = 0; k < DIGITS.length; k++) {
//                        if (uno.equals(DIGITS[k])) {
//                            triplet[1] = 0;
//                            triplet[2] = k + 1;
//                        }
//                        if (uno.equals(TENS[k])) {
//                            triplet[1] = k + 1;
//                            triplet[2] = 0;
//                        }
//                    }
//                }
//
//
//                else if (set.countTokens() == 2) {  //If there are two tokens given in triplet
//                    String uno = set.nextToken();
//                    String dos = set.nextToken();
//                    if (dos.equals(MAGNITUDES[0])) {  //If one of the two tokens is "hundred"
//                        for (int k = 0; k < DIGITS.length; k++) {
//                            if (uno.equals(DIGITS[k])) {
//                                triplet[0] = k + 1;
//                                triplet[1] = 0;
//                                triplet[2] = 0;
//                            }
//                        }
//                    }
//                    else {
//                        triplet[0] = 0;
//                        for (int k = 0; k < DIGITS.length; k++) {
//                            if (uno.equals(TENS[k])) {
//                                triplet[1] = k + 1;
//                            }
//                            if (dos.equals(DIGITS[k])) {
//                                triplet[2] = k + 1;
//                            }
//                        }
//                    }
//                }
//
//                else if (set.countTokens() == 3) {  //If there are three tokens given in triplet
//                    String uno = set.nextToken();
//                    String dos = set.nextToken();
//                    String tres = set.nextToken();
//                    for (int k = 0; k < DIGITS.length; k++) {
//                        if (uno.equals(DIGITS[k])) {
//                            triplet[0] = k + 1;
//                        }
//                        if (tres.equals(DIGITS[k])) {
//                            triplet[1] = 0;
//                            triplet[2] = k + 1;
//                        }
//                        if (tres.equals(TENS[k])) {
//                            triplet[1] = k + 1;
//                            triplet[2] = 0;
//                        }
//                    }
//                }
//
//                else if (set.countTokens() == 4) {  //If there are four tokens given in triplet
//                    String uno = set.nextToken();
//                    String dos = set.nextToken();
//                    String tres = set.nextToken();
//                    String cuatro = set.nextToken();
//                    for (int k = 0; k < DIGITS.length; k++) {
//                        if (uno.equals(DIGITS[k])) {
//                            triplet[0] = k + 1;
//                        }
//                        if (cuatro.equals(DIGITS[k])) {
//                            triplet[2] = k + 1;
//                        }
//                        if (tres.equals(TENS[k])) {
//                            triplet[1] = k + 1;
//                        }
//                    }
//                }
//                else {
//                    triplet[0] = 0;
//                    triplet[1] = 0;
//                    triplet[2] = 0;
//                }
//
//                result = result + Integer.toString(triplet[0]) + Integer.toString(triplet[1]) + Integer.toString(triplet[2]);
//            }
//        }
//
//        if (decimal.length > 1) {  //The number is a decimal
//            StringTokenizer decimalDigits = new StringTokenizer(decimal[1]);
//            result = result + ".";
//            System.out.println(decimalDigits.countTokens() + " decimal digits");
//            while (decimalDigits.hasMoreTokens()) {
//                String w = decimalDigits.nextToken();
//                System.out.println(w);
//
//                if (w.equals(ZERO[0]) || w.equals(ZERO[1])) {
//                    result = result + "0";
//                }
//                for (int j = 0; j < DIGITS.length; j++) {
//                    if (w.equals(DIGITS[j])) {
//                        result = result + Integer.toString(j + 1);
//                    }
//                }
//
//            }
//        }
//
//        return result;
//    }


//    public static Integer parseNumber(String s){
//        s = s.replaceAll("^a ", "");
//        s = s.replace(" a ", " ");
//        s = s.replace(" and ", " ");
//
//
//        for (int m = MAGNITUDES.length -1; m > -1; m--){
//            int pos = s.indexOf(MAGNITUDES[m]);
//            if(pos != -1){
//                Integer lhs;
//                if(pos == 0) lhs = 1;
//                else lhs = parseNumber(s.substring(0, pos));
//
//                String rhsString = s.substring(pos + MAGNITUDES[m].length()).trim();
//
//                Integer rhs = rhsString.isEmpty() ? 0 : parseNumber(rhsString);
//
//                return lhs == null || rhs == null ? null :  lhs * MAG_VALS[m] + rhs;
//            }
//        }
//
//        String[] tokens = s.split("\\s");
//        String parsedTokens = "";
//        for (int t = 0; t < tokens.length; t++) {
//            String token = tokens[t];
//            Integer parsedValue = null;
//            try {
//                parsedValue = Integer.parseInt(token);
//            } catch (Exception ignored) {
//            }
//            if (parsedValue != null) {
//                parsedTokens += parsedValue;
//                continue;
//            }
//
//            for (int i = 0; i < TENS.length; i++) {
//                if (TENS[i].equals(token)) {
//                    parsedValue = TEN_VALS[i];
//                    break;
//                }
//            }
//            if (parsedValue != null) {
//                parsedTokens += parsedValue;
//                continue;
//            }
//
//            for (int i = 0; i < TEENS.length; i++) {
//                if (TEENS[i].equals(token)) {
//                    parsedValue = TEEN_VALS[i];
//                }
//            }
//            if (parsedValue != null) {
//                parsedTokens += parsedValue;
//                continue;
//            }
//
//            for (int i = 0; i < DIGITS.length; i++) {
//                if (DIGITS[i].equals(token)) {
//                    parsedValue = DIGIT_VALS[i];
//                }
//            }
//            if (parsedValue != null) {
//                parsedTokens += parsedValue;
//                continue;
//            }
//
//            return null;
//        }
//
//        try {
//            return Integer.parseInt(parsedTokens);
//        } catch (Exception ignored){}
//
//        return null;
//    }


    public static Integer parseNumber(String s) {
        s=s.replace(",", "");
        StringBuilder cleanString = new StringBuilder(s.length());
        for (String token : s.split("(\\s+|\\$)")) {
            boolean intLiteral = false;
            try {
                Integer.parseInt(token);
                intLiteral = true;
            }catch (Exception ignored){}

            if(intLiteral ||
                    ArrayUtils.contains(MAGNITUDES, token) ||
                    ArrayUtils.contains(TENS, token) ||
                    ArrayUtils.contains(TEENS, token) ||
                    ArrayUtils.contains(DIGITS, token)) {
                cleanString.append(token).append(" ");
            }
        }

        return _parseNumber(cleanString.toString().trim());
    }
    private static Integer _parseNumber(String s){

        for (int m = MAGNITUDES.length -1; m > -1; m--){
            int pos = s.indexOf(MAGNITUDES[m]);
            if(pos != -1){
                Integer lhs;
                if(pos == 0) lhs = 1;
                else lhs = parseNumber(s.substring(0, pos));

                String rhsString = s.substring(pos + MAGNITUDES[m].length()).trim();

                Integer rhs;
                if(rhsString.isEmpty()) rhs = 0;
                else rhs = parseNumber(rhsString);

                int result;

                return lhs == null || rhs == null ? null :  lhs * MAG_VALS[m] + rhs;
            }
        }


        for (int tens = 0; tens < TENS.length; tens++){
            int pos = s.indexOf(TENS[tens]);
            if(pos != -1){
                Integer lhs;
                if(pos == 0) lhs = 0;
                else lhs = parseNumber(s.substring(0, pos));

                String rhsString = s.substring(pos + TENS[tens].length()).trim();

                //peek for digit after ten
                int peekedDigit = 0;
                int indexOfFirstSpace = rhsString.indexOf(" ");
                String firstToken = indexOfFirstSpace == -1 ? rhsString : rhsString.substring(0, indexOfFirstSpace);
                for (int d = 0; d < DIGITS.length; d++) {
                    String digit = DIGITS[d];
                    if (digit.equals(firstToken)) {
                        peekedDigit = DIGIT_VALS[d];
                        rhsString = indexOfFirstSpace == -1? "" : rhsString.substring(indexOfFirstSpace);
                        break;
                    }
                }

                Integer rhs;
                if(rhsString.isEmpty()) return lhs == null ? null :  Integer.parseInt(String.format("%d%d", lhs, (TEN_VALS[tens] + peekedDigit)));
                else rhs = parseNumber(rhsString);

                return lhs == null || rhs == null ? null :  Integer.parseInt(String.format("%d%d%d", lhs, (TEN_VALS[tens] + peekedDigit),  rhs));
            }
        }



        String[] tokens = s.split("\\s");
        String parsedTokens = "";
        for (int t = 0; t < tokens.length; t++) {
            String token = tokens[t];
            Integer parsedValue = null;
            try {
                parsedValue = Integer.parseInt(token);
            } catch (Exception ignored) {
            }
            if (parsedValue != null) {
                parsedTokens += parsedValue;
                continue;
            }

//            for (int i = 0; i < TENS.length; i++) {
//                if (TENS[i].equals(token)) {
//                    parsedValue = TEN_VALS[i];
//                    break;
//                }
//            }
//            if (parsedValue != null) {
//                parsedTokens += parsedValue;
//                continue;
//            }

            for (int i = 0; i < TEENS.length; i++) {
                if (TEENS[i].equals(token)) {
                    parsedValue = TEEN_VALS[i];
                }
            }
            if (parsedValue != null) {
                parsedTokens += parsedValue;
                continue;
            }

            for (int i = 0; i < DIGITS.length; i++) {
                if (DIGITS[i].equals(token)) {
                    parsedValue = DIGIT_VALS[i];
                }
            }
            if (parsedValue != null) {
                parsedTokens += parsedValue;
                continue;
            }

            return null;
        }

        try {
            return Integer.parseInt(parsedTokens);
        } catch (Exception ignored){}

        return null;
    }

}
