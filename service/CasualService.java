package com.example.totaldemo.service;


import java.util.Map;

public interface CasualService {
    Map<String, Object> buyBook(Integer bookId);

    Map<String,Object> bookList();
}
