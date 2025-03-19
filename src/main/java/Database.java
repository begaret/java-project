import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Database implements IDatabase
{
    static final Logger logger = LogManager.getLogger(Database.class);
    Connection connection;

    Database()
    {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=libraryDB;encrypt=false;trustServerCertificate=true";
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
            Date.valueOf(member.suspended),
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
        String sql = String.format("DELETE FROM Member WHERE id = %d;", id);
        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return false;
        }
    }


    public int delay_member(int id)
    {
        Member member = get_member(id);
        if (member == null) {
            return -1;
        }

        String sql = String.format("UPDATE Member SET delays = %d WHERE id = %d;",
            member.delays + 1, id);
        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            return ++member.delays;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return member.delays;
        }
    }

    public boolean suspend_member(int id)
    {
        LocalDate suspended = LocalDate.now().plusDays(15);

        String sql = String.format("UPDATE Member SET suspended = '%s', delays = 0 WHERE id = %d;",
                Date.valueOf(suspended), id);
        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return false;
        }
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
            return new ArrayList<>();
        }
    }

    public boolean add_loan(Loan loan)
    {
        String sql = String.format("INSERT INTO Loan"
                        + " (id, isbn, date)"
                        + " VALUES ('%d', '%s', '%s');",
                loan.id,
                loan.ISBN,
                Date.valueOf(loan.when));
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
        String sql = String.format("DELETE FROM Loan WHERE id = %d AND isbn = '%s';", id, ISBN);
        logger.debug("Executing query '{}'", sql);
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.error("Query failed: {}", e.getMessage());
            return false;
        }
    }
}
