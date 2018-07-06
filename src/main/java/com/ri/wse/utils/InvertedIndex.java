package com.ri.wse.utils;

import org.w3c.dom.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvertedIndex {
    public Hashtable<String, Map<Integer, Integer>> arquivo = new Hashtable<>();
    public Map<String, Integer> termoFrequencia = new HashMap<>();
    public Set<String> bagOfWord = new HashSet<>();
    Map<Integer,String> documentIndice=new HashMap<>();

    public InvertedIndex() throws IOException {
        List<String> lines = Files.readAllLines(new File(getClass().getResource("/file/data.csv").getPath()).toPath());
        List<String> attrs = Arrays.asList(lines.get(0).split(","));
        lines = lines.subList(1,lines.size());
        //URL,nome,telefone,CRP,preço


        Map<Integer, Map<String, String[]>> documentTerms=new HashMap<>();
        int i=0;
        for(String line : lines) {
            String[] values = line.split(",");
            Map<String, String[]> documentMap = new HashMap<>();
            for (int j= 0; j < attrs.size(); j++) {
                final int jj = j;
                documentMap.put(attrs.get(j), values[j].split(" +"));
                bagOfWord.addAll(
                        Arrays.stream(documentMap.get(attrs.get(j)))
                                .map(word -> attrs.get(jj) + "." + word)
                                .collect(Collectors.toList()));
            }

            documentIndice.put(i,documentMap.get("URL")[0]);
            documentMap.remove("URL");
            documentTerms.put(i,documentMap);
            i++;
        }
        for(String term : bagOfWord){
            for(Map.Entry<Integer, Map<String, String[]>> documentTerm:documentTerms.entrySet()) {
                System.out.println(Arrays.toString(documentTerm.getValue().getOrDefault(term, new String[0])));
                Map<Integer, Integer> documentIdf = arquivo.getOrDefault(term, new HashMap<>());
                documentIdf.put(documentTerm.getKey(), 1);
                arquivo.put(term, documentIdf);
            }
        }
        arquivo.entrySet().stream()
                .forEach(entry -> termoFrequencia.put(
                        entry.getKey(),
                        entry.getValue()
                                .values()
                                .stream()
                                .mapToInt(integer -> integer)
                                .sum()
                        )
                );
    }


}


