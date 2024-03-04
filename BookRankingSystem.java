package com.megasoftware.bookranking;
// @author prabhashana
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class BookRankingSystem {
    
    private static final String URL = "jdbc:mysql://localhost:3306/book_ranking";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public void addBook(String title, String author, int rank) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // Create statement
            String sql = "INSERT INTO books (title, author, rank) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setInt(3, rank);

            // Execute query
            statement.executeUpdate();
            AddBookFrame addBookFrame = new AddBookFrame();
            JOptionPane.showMessageDialog(addBookFrame, "Book added");
        } catch (SQLException e) {
            AddBookFrame addBookFrame = new AddBookFrame();
            JOptionPane.showMessageDialog(addBookFrame, e);
        }
    }
    
    public Book searchBook(String title) {
        for (Book book : loadBookRankings()) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null; // Book not found
    }
    
    
    // Method to load book rankings from database and perform merge sort
    public ArrayList<Book> loadBookRankings() {
        ArrayList<Book> books = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // Create statement
            String sql = "SELECT * FROM books";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Populate books ArrayList
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int rank = resultSet.getInt("rank");
                books.add(new Book(title, author, rank));
            }

            // Perform merge sort
            mergeSort(books, 0, books.size() - 1);
        } catch (SQLException e) {
            BookRankingFrame bookRankingFrame = new BookRankingFrame();
            JOptionPane.showMessageDialog(bookRankingFrame, e);
        }
        return books;
    }

    // Merge sort for sorting books
    private void mergeSort(ArrayList<Book> books, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(books, left, mid);
            mergeSort(books, mid + 1, right);
            merge(books, left, mid, right);
        }
    }

    private void merge(ArrayList<Book> books, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        ArrayList<Book> leftArray = new ArrayList<>();
        ArrayList<Book> rightArray = new ArrayList<>();

        for (int i = 0; i < n1; ++i)
            leftArray.add(books.get(left + i));
        for (int j = 0; j < n2; ++j)
            rightArray.add(books.get(mid + 1 + j));

        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (leftArray.get(i).getRank() >= rightArray.get(j).getRank()) {
                books.set(k, leftArray.get(i));
                i++;
            } else {
                books.set(k, rightArray.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            books.set(k, leftArray.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            books.set(k, rightArray.get(j));
            j++;
            k++;
        }
    }

}
