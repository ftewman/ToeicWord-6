package com.groupthree.dictionary.model;

/**
 * Created by Kent on 3/22/2016.
 */
public class Subject {
    public int Id;
    public String Subject;
    public String SubjectMean;
    public int countFavouriteWord;

    public Subject(int Id, String subject, String subjectMean, int countFavouriteWord) {
        this.Id = Id;
        this.Subject = subject;
        this.SubjectMean = subjectMean;
        this.countFavouriteWord = countFavouriteWord;
    }
}
