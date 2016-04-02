package com.groupthree.dictionary.model;

public class Word {
	public int id;
	public String Subject;
	public String SubjectMean;
	public String Word;
	public String Type;
	public String Phonetic;
	public String Mean;
	public String SortMean;
	public String Synonyms;
	public String Sentence;
	public String SentenceMean;
	public int FavouriteWord;
	public String AdequateMean;
	public String Pharagraph;
	public int View;

	public Word(int id, String subject, String subjectMean, String word,
			String type, String phonetic, String mean, String sortMean,
			String synonyms, String sentence, String sentenceMean,
			int favouriteWord, String adequateMean, String pharagraph, int view) {
		super();
		this.id = id;
		Subject = subject;
		SubjectMean = subjectMean;
		Word = word;
		Type = type;
		Phonetic = phonetic;
		Mean = mean;
		SortMean = sortMean;
		Synonyms = synonyms;
		Sentence = sentence;
		SentenceMean = sentenceMean;
		FavouriteWord = favouriteWord;
		AdequateMean = adequateMean;
		Pharagraph = pharagraph;
		View = view;
	}

	@Override
	public String toString() {
		return id + " " + Word + "(" + Type + ")" + "\n" + Mean + "\n"
				+ Sentence + "\n" + SentenceMean;
	}

}
