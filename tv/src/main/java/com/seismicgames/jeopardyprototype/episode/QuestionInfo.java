package com.seismicgames.jeopardyprototype.episode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jduffy on 7/7/16.
 */
public class QuestionInfo {
    public final String category;
    public final String clue;
    public final String[] answers;
    public final int value;
    public final int readTimestamp;
    public final int answerTimestamp;

    public QuestionInfo(String category, String clue, String answerString, int value, String read_timestamp, String answer_timestamp) {
        this.category = category;
        this.clue = clue;
        this.answers = parseAnswer(answerString);
        this.value = value;
        this.readTimestamp = parseTimestamp(read_timestamp);
        this.answerTimestamp = parseTimestamp(answer_timestamp);
    }


    private int parseTimestamp(String ts) {
        int millis = 116000;
        millis += Integer.parseInt(ts.substring(3, 5)) * 60 * 1000;
        millis += Integer.parseInt(ts.substring(6, 8)) * 1000;
        millis += Integer.parseInt(ts.substring(9, 11)) * 1000d / 29.97d;
        return millis;
    }


    private static String[] parseAnswer(String answerString) {
        String[] splitAnswer = answerString.toLowerCase().split("\\|");
        Set<String> answers = new HashSet<>();

        answers.addAll(Arrays.asList(splitAnswer));

        List<ReplacementAlgorithm> replaceList = new ArrayList<>();

        replaceList.add(new ReplacementAlgorithm() {
            @Override
            public String replace(String s) {
                return removePossessiveS(s);
            }
        });
        replaceList.add(new ReplacementAlgorithm() {
            @Override
            public String replace(String s) {
                return removePeriods(s);
            }
        });
        replaceList.add(new ReplacementAlgorithm() {
            @Override
            public String replace(String s) {
                return removeApostrophe(s);
            }
        });
        replaceList.add(new ReplacementAlgorithm() {
            @Override
            public String replace(String s) {
                return removeDash(s);
            }
        });
        replaceList.add(new ReplacementAlgorithm() {
            @Override
            public String replace(String s) {
                return replaceDash(s);
            }
        });
        replaceList.add(new ReplacementAlgorithm() {
            @Override
            public String replace(String s) {
                return replaceAmpersand(s);
            }
        });

        List<List<ReplacementAlgorithm>> replacementStrategies = permutation(replaceList);

        for (String answer : new ArrayList<>(answers)) {
            for (List<ReplacementAlgorithm> algorithm : replacementStrategies) {
                String s = answer;
                for (ReplacementAlgorithm algPart : algorithm) {
                    s = algPart.replace(s);
                }
                answers.add(s.replaceAll("\\s+", " "));
            }
        }

        return answers.toArray(new String[answers.size()]);

    }


    private static String removePossessiveS(String s) {
        return s.replace("'s", "");
    }

    private static String removePeriods(String s) {
        return s.replace(".", "");
    }

    private static String removeApostrophe(String s) {
        return s.replace("'", "");
    }

    private static String removeDash(String s) {
        return s.replace("-", "");
    }

    private static String replaceDash(String s) {
        return s.replace("-", " ");
    }

    private static String replaceAmpersand(String s) {
        return s.replace("&", " and ");
    }

    interface ReplacementAlgorithm {
        String replace(String s);
    }


    public static <T> List<List<T>> permutation(List<T> s) {
        // The result
        List<List<T>> res = new ArrayList<List<T>>();
        // If input string's length is 1, return {s}
        if (s.size() == 1) {
            res.add(s);
        } else if (s.size() > 1) {
            int lastIndex = s.size() - 1;
            // Find out the last character
            T last = s.get(lastIndex);
            // Rest of the string
            List<T> rest = s.subList(0, lastIndex);
            // Perform permutation on the rest string and
            // merge with the last character
            res = merge(permutation(rest), last);
        }
        return res;
    }

    /**
     * @param list a result of permutation, e.g. {"ab", "ba"}
     * @param c    the last character
     * @return a merged new list, e.g. {"cab", "acb" ... }
     */
    public static <T> List<List<T>> merge(List<List<T>> list, T c) {
        ArrayList<List<T>> res = new ArrayList<List<T>>();
        res.addAll(list);
        // Loop through all the string in the list
        for (List<T> s : list) {
            // For each string, insert the last character to all possible postions
            // and add them to the new list
            for (int i = 0; i <= s.size(); ++i) {
                List<T> ps = new ArrayList<T>(s);
                ps.add(i, c);
                res.add(ps);
            }
        }
        return res;
    }

}
