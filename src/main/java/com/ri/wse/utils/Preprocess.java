package com.ri.wse.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocess {
    private static String stopwords = "stopwords.txt";
    private static Hashtable sw = null;

    public Preprocess(String truePages, String falsePages) throws IOException {
        int i = 0;
        int j = 0;
        //TRUE
        List<String> sitesTrue = getFiles(truePages);
        for (String site : sitesTrue) {
         parse(site, i);
            i+=1;
        }

        //FALSE
        List<String> sitesFalse = getFiles(falsePages);
        for (String site : sitesFalse) {
            //parse(site, i);
            j+=1;
        }

    }

    public static List<String> getFiles(String path) {
        List<String> r = new ArrayList<String>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File f : files) {
            String p = f.getAbsolutePath();
            if (f.isDirectory()) {
                r.addAll(getFiles(p));
            } else {
                r.add(p);
            }
        }
        return r;
    }

    public static String readFile(String path) throws IOException {
        String r = new String(Files.readAllBytes(Paths.get(path)));
        return r.replaceAll("\\n|\\r", " ");
    }


    public static boolean isStopWord(Hashtable stopWords, String word) {
        boolean result = false;
        if (stopWords != null) {
            if (stopWords.containsKey(word)) {
                result = true;
            }
        }
        return result;
    }

    public static void initializeStopWords() throws IOException {
        if (stopwords != null) { //se tiver stopwords
            sw = new Hashtable<String, Integer>();
            String[] sws = readFile(stopwords).split(" ");
            for (String s : sws) { //grava-se elas numa hashtable para verificar dps
                if (s.length() > 0) {
                    sw.put(s, 0);
                }
            }
        }
    }

    public static List parse(String file, int i) throws IOException {

        String doc = readFile(file);
        List listWords = new ArrayList<String>();
        listWords = getTitle(doc);

        doc = doc.replaceAll("<[^>]*head[^>]*>(.*?)<[^>]*/[^>]*head[^>]*>", "");
        doc = doc.replaceAll(
                "<[\\s|\\t|\\n|\\r]*script[^<>]*>(.*?)<[\\s|\\t|\\n|\\r]*/[\\s|\\t|\\n|\\r]*script[^<>\\\"]*>", "");
        doc = doc.replaceAll(
                "<[\\s|\\t|\\n|\\r]*style[^<>]*>(.*?)<[\\s|\\t|\\n|\\r]*/[\\s|\\t|\\n|\\r]*style[^<>\\\"]*>", "");
        doc = doc.replaceAll("<(.*?)>", " ");
        doc = doc.replaceAll("[.] |, | ,|\\||\\!|\\?|:|'|;| & |\\(|\\)| - |=|#|&", " ");
        String sep = "-:-";
        doc = doc.replaceAll("\\s+| +", sep);
        doc = doc.replaceAll("^" + sep, "");
        String[] words = doc.split(sep);
        for (String w : words) {
            initializeStopWords();
            if(!isStopWord(sw, w)) {
                listWords.add(w + " ");
            }
        }
        new writer().writerFile(listWords, i);
        return listWords;
    }

    public static List getTitle(String doc) {
        String[] sub = doc.split("<");
        List<String> r = new ArrayList<String>();
        Pattern pattern = Pattern.compile("^[\\s]*title[^>]*>(.*)");
        for (String l : sub) {
            Matcher matcher = pattern.matcher(l);
            if (matcher.find()) {
                String contentTitle = matcher.group(1);
                String[] wordsTitle = contentTitle.split(" ");
                for (String w : wordsTitle) {
                    if (!w.equals("")) {
                        w = w.replaceAll("'", "");
                        r.add("title." + w + " ");
                    }
                }
                break;
            }
        }
        return r;
    }
}
