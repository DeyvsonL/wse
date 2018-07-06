package com.ri.wse.controller;

import com.ri.wse.utils.InvertedIndex;
import com.ri.wse.utils.Ranker;
import javafx.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class WseController {


    @RequestMapping(value = "/livre", method = RequestMethod.GET)
    public ModelAndView login(String query){
        System.out.println(query);
        List<String> queryItems = new ArrayList<>();
        queryItems.add(query);
        List<URL> items=null;
        //List<URL> items = RANK.rank(queryItems,true);
        ModelAndView modelAndView = new ModelAndView("/result");
        modelAndView.addObject("");
        return modelAndView;
    }

    @RequestMapping(value = "/atributo", method = RequestMethod.GET)
    public ModelAndView login(String nome, String crp, String telefone, String preco) throws IOException {
        List<String> queryItems = new ArrayList<>();
        if(nome!=null&&!nome.isEmpty()){
            nome="nome."+nome;
            queryItems.add(nome);
        }
        if(crp!=null&&!crp.isEmpty()){
            crp="CRP."+crp;
            queryItems.add(crp);
        }
        if(telefone!=null&&!telefone.isEmpty()){
            telefone="telefone."+telefone;
            queryItems.add(telefone);
        }
        if (preco != null&&!preco.isEmpty()) {
            preco = "preço." + preco;
            queryItems.add(preco);
        }
        InvertedIndex ii = new InvertedIndex();
        Ranker ranker = new Ranker(ii);
        List<String> items = ranker.rank(queryItems,false);
        ModelAndView modelAndView = new ModelAndView("/result");
        modelAndView.addObject("items",items);
        return modelAndView;
    }
}
