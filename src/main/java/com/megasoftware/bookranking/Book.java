
package com.megasoftware.bookranking;

/**
 *
 * @author prabhashana
 */
public class Book {
    private String title;
    private String author;
    private int rank;

    // Constructor
    public Book(String title, String author, int rank) {
        this.title = title;
        this.author = author;
        this.rank = rank;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getRank() {
        return rank;
    }
    
}
