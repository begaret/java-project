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
    public int id;
    // 1 = student, 2 = teacher and so on.
    public int level;
    public LocalDate suspended;
    public int delays;

    Member()
    {
        suspended = LocalDate.MIN;
        delays = 0;
    }

    Member(String fname, String lname, int id, int level, LocalDate suspended)
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
        return switch (level) {
            case 0 -> 3;
            case 1 -> 5;
            case 2 -> 7;
            case 3 -> 10;
            case 4 -> Integer.MAX_VALUE;
            default -> 0;
        };

    }
}
