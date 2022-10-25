package org.ruff.fastsearch;

public class Candidate {

    private Integer id;
    private String content;

    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String TAG = "tag";

    public Candidate() {
    }

    public Candidate(Integer i, String content) {
	this.id = i;
	this.content = content;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }
}