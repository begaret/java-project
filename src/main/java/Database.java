import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database
{
    static final Logger logger = LogManager.getLogger(Database.class);
    Connection connection;

    Database(String url)
    {
        try {
            String dbUrl = "jdbc:sqlserver://172.27.146.207:1433;databaseName=libraryDB;encrypt=false;trustServerCertificate=true";
            String user = "remote_userr";
            String password = "1234";

            connection = DriverManager.getConnection(dbUrl, user, password);
            logger.info("Connected to database");
        } catch (SQLException e) {
            logger.error("Failed to connect: {}", e.getMessage());
        }
    }

    // MEMBER
    public boolean set_member(Member member) { return false; }
    public Member get_member(String id) { return null; }
    public boolean add_member(Member member) { return false; }
    public boolean remove_member(String id) { return false; }

    // BOOK
    public Book get_book(String ISBN) { return null; }

    // LOAN
    public boolean set_loan(Loan loan) { return false; }

    /*  return loans for each arg
    *   if arg is empty do not use in query
    *   if no loans found, return empty array
    */
    public Loan[] get_loans(String id, String ISBN) { return null; }
    public boolean add_loan(Loan loan) { return false; }
    public boolean remove_loan(String id, String ISBN) { return false; }
}
