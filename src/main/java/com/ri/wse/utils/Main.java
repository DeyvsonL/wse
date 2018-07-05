package com.ri.wse.utils;

import java.io.IOException;
import java.util.Hashtable;

public class Main {

    public static void main(String[] args) throws IOException {

        try {
           // new Preprocess("./pag", "");
             new InvertedIndex();
             boolean a = false;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
