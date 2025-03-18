import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

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

    public void shutdown()
    {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Failed to close: {}", e.getMessage());
        }
    }

    // MEMBER
    public boolean set_member(Member member) { return false; }
    public Member get_member(int id)
    {
        try {
            logger.debug(String.format("SELECT * FROM Member WHERE id = %s", id));

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM Member WHERE id = %s", id));
            if (!rs.next()) {
                logger.warn("No members found");
                return null;
            }

            Member member = new Member();
            member.id = rs.getInt(0);
            member.first_name = rs.getString(1);
            member.last_name = rs.getString(2);
            member.level = rs.getInt(3);
            member.suspended = rs.getDate(4).toLocalDate();
            member.delays = rs.getInt(5);
            return member;
        } catch (SQLException e) {
            logger.error("Failed to close: {}", e.getMessage());
            return null;
        }
    }
    public boolean add_member(Member member) { return false; }
    public boolean remove_member(int id) { return false; }

    // BOOK
    public Book get_book(String ISBN) { return null; }

    // LOAN
    public boolean set_loan(Loan loan) { return false; }

    /*  return loans for each arg
    *   if arg is empty do not use in query
    *   if no loans found, return empty array
    */
    public Loan[] get_loans(int id, String ISBN) { return null; }
    public boolean add_loan(Loan loan) { return false; }
    public boolean remove_loan(int id, String ISBN) { return false; }
}
