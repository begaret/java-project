import java.util.ArrayList;

public interface IDatabase
{
    public void shutdown();
    public boolean is_connected();
    public Member get_member(int id);
    public boolean add_member(Member member);
    public boolean remove_member(int id);
    public int delay_member(int id);
    public boolean suspend_member(int id);
    public Book get_book(String ISBN);
    public ArrayList<Loan> get_loans(int id, String ISBN);
    public boolean add_loan(Loan loan);
    public boolean remove_loan(int id, String ISBN);
}
