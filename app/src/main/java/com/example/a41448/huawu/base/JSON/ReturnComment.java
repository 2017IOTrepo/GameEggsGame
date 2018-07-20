package com.example.a41448.huawu.base.JSON;

import java.util.ArrayList;
import java.util.List;

public class ReturnComment {
    List<GetComment> comments = new ArrayList<>();
    public void add(GetComment getComment){
        comments.add(getComment);
    }

    public List<GetComment> getComments() {
        return comments;
    }

    public void setComments(List<GetComment> comments) {
        this.comments = comments;
    }
}
