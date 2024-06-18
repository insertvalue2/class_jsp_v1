package com.tenco.mvc.models;

import java.sql.Date;

import lombok.Data;

@Data
public class TodoDTO {
	private int id;
    private int userId;
    private String title;
    private String description;
    private Date dueDate;
    private boolean completed;
}
