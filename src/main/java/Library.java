import java.time.LocalDate;

public class Library
{
    Database db;
    Member user;

    Library(Database _db)
    {
        db = _db;
    }

    public boolean login(int id)
    {
        user = db.get_member(id);
        return user != null;
    }

    public boolean is_suspended()
    {
        System.out.printf("%s >= %s%n", user.suspended.toString(), LocalDate.now());

        LocalDate now = LocalDate.now();
        return !user.suspended.isBefore(now);
    }

    public boolean user_has_level(int level)
    {
        return user.level == level;
    }

    public boolean lend_book(String ISBN)
    {
        int borrowed = db.get_loans(user.id, "").size();
        if (borrowed >= user.max_books()) {
            System.out.println("You cannot lend any more books!");
            return false;
        }

        Book book = db.get_book(ISBN);
        if (book == null) {
            System.out.printf("No book with ISBN = %s%n", ISBN);
            return false;
        } else if (db.get_loans(-1, ISBN).size() >= book.amount) {
            System.out.println("Book is not available");
            return false;
        }

        // borrow
        Loan loan = new Loan();
        loan.id = user.id;
        loan.ISBN = ISBN;
        loan.when = LocalDate.now();
        return db.add_loan(loan);
    }

    public boolean return_book(String ISBN)
    {
        Book book = db.get_book(ISBN);
        if (book == null) {
            System.out.printf("No book with ISBN = %s%n",
                    ISBN);
            return false;
        }

        db.remove_loan(user.id, ISBN);
        return true;
    }

    public boolean create_member(Member user)
    {
        Member member = db.get_member(user.id);
        if (member == null) {
            // TODO: generate id
            return db.add_member(user);
        }

        LocalDate now = LocalDate.now();
        if (!member.suspended.isBefore(now)) {
            System.out.println("User is suspended");
            return false;
        }

        System.out.println("User already exists");
        return false;
    }

    public boolean delete_member(int id)
    {
        return db.remove_member(id);
    }

    public boolean suspend_member(int id)
    {
        Member member = db.get_member(id);
        if (member == null) {
            System.out.println("User does not exist");
            return false;
        }

        member.delays++;
        if (member.delays > 2) {
            member.suspended = LocalDate.now().plusDays(15);
            member.delays = 0;
        }

        return true;
    }
}
