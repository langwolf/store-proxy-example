package com.lioncorp.example.store.proxy.domain;

public class Item {

    private String docId;

    private Integer score;

    private Integer rank;

    public String getDocId() {
        return docId;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getRank() {
        return rank;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
