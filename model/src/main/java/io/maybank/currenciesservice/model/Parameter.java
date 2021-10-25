package io.maybank.currenciesservice.model;

import lombok.*;

import java.util.HashMap;
import java.util.Map;


public class Parameter{
    @Getter @Setter
    private String baseUrl;
    @Getter @Setter
    private String context;
    @Getter @Setter
    private String method;
    @Getter @Setter
    private Map<String,Object> queryStr=new HashMap<>();
    @Getter @Setter
    private Map<String,Object> header=new HashMap<>();
    @Getter @Setter
    private Map<String,Object> body=new HashMap<>();
}