import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class Database
{
    static final Logger logger = LogManager.getLogger(Database.class);
    Connection connection;

    Database()
    {
        try {
            String url = "jdbc:sqlserver://172.27.146.207:1433;databaseName=libraryDB;encrypt=false;trustServerCertificate=true";
            String user = "remote_userr";
            String password = "1234";

            connection = DriverManager.getConnection(url, user, password);
            logger.info("Connected to database");
        } catch (SQLException e) {
            logger.fatal("Failed to connect: {}", e.getMessage());
        }
    }

    public void shutdown()
    {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Failed to close: {}", e.getMessage());
        }
    }

    public boolean is_connected() { return connection != null; }

    // MEMBER
    public Member get_member(int id)
    {
        String sql = String.format("SELECT * FROM Member WHERE id = %d;", id);
        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                logger.warn("Query returned null");
                return null;
            }

            Member member = new Member();
            member.id = rs.getInt("id");
            member.first_name = rs.getString("firstname");
            member.last_name = rs.getString("lastname");
            member.level = rs.getInt("level");
            member.suspended = rs.getDate("suspended").toLocalDate();
            member.delays = rs.getInt("delays");
            return member;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return null;
        }
    }

    public boolean add_member(Member member)
    {
        String sql = String.format("INSERT INTO Member"
                                 + " (id, firstname, lastname, level, suspended, delays)"
                                 + " VALUES ('%d', '%s', '%s', %d, '%s', %d);",
            member.id,
            member.first_name,
            member.last_name,
            member.level,
            member.suspended.toString(),
            member.delays);
        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean remove_member(int id)
    {
        /*
        String sql = String.format("DELETE FROM Member WHERE id = %d;", id);
        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                logger.warn("Query returned null");
                return null;
            }

            Book book = new Book();
            book.author = rs.getString("author");
            book.ISBN = rs.getString("isbn");
            book.title = rs.getString("title");
            book.year = rs.getInt("year");
            book.amount = rs.getInt("amount");
            return book;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return null;
        }
        */

        return false;
    }

    // BOOK
    public Book get_book(String ISBN)
    {
        String sql = String.format("SELECT * FROM books WHERE isbn = '%s';", ISBN);
        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                logger.warn("Query returned null");
                return null;
            }

            Book book = new Book();
            book.author = rs.getString("author");
            book.ISBN = rs.getString("isbn");
            book.title = rs.getString("title");
            book.year = rs.getInt("year");
            book.amount = rs.getInt("amount");
            return book;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return null;
        }
    }

    // LOAN
    /*  return loans for each arg
    *   if arg is empty do not use in query
    *   if no loans found, return empty array
    */
    public ArrayList<Loan> get_loans(int id, String ISBN)
    {
        String sql = "SELECT * FROM Loan WHERE";
        if (id >= 0) {
            sql += String.format(" id = %d", id);
            if (!ISBN.isEmpty()) {
                sql += String.format(" AND isbn = '%s'", ISBN);
            }

            sql += ";";
        } else {
            sql += String.format(" isbn = '%s';", ISBN);
        }

        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                logger.warn("Query returned null");
                return null;
            }

            ArrayList<Loan> loans = new ArrayList<>();
            while (rs.next()) {
                Loan loan = new Loan();
                loan.id = rs.getInt("id");
                loan.ISBN = rs.getString("isbn");
                loan.when = rs.getDate("date").toLocalDate();
                loans.add(loan);
            }

            return loans;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return null;
        }
    }

    public boolean add_loan(Loan loan)
    {
        String sql = String.format("INSERT INTO Loan"
                        + " (id, isbn, date)"
                        + " VALUES ('%d', '%s', '%s');",
                loan.id,
                loan.ISBN,
                loan.when.toString());
        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean remove_loan(int id, String ISBN)
    {
        return false;
    }
}
