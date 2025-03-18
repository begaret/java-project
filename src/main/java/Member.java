import java.time.LocalDate;

public class Member
{
    public static int undergrad = 0;
    public static int postgrad = 1;
    public static int student = 2;
    public static int teacher = 3;
    public static int librarian = 4;

    public String first_name;
    public String last_name;
    public String id;
    // 1 = student, 2 = teacher and so on.
    public int level;
    public LocalDate suspended;
    public int delays;

    Member()
    {
        suspended = LocalDate.MIN;
        delays = 0;
    }

    Member(String fname, String lname, String id, int level, LocalDate suspended)
    {
        this.first_name = fname;
        this.last_name = lname;
        this.id = id;
        this.level = level;
        this.suspended = suspended;
        delays = 0;
    }

    public int max_books()
    {
        switch (level) {
        case 0: return 3;
        case 1: return 5;
        case 2: return 7;
        case 3: return 10;
        case 4: return Integer.MAX_VALUE;
        }

        return 0;
    }
}
