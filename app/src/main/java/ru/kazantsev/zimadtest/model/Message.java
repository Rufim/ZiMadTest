package ru.kazantsev.zimadtest.model;

import java.util.List;

import lombok.Data;

@Data
public class Message {
    private String text;
    private List<Pet> data;
}
