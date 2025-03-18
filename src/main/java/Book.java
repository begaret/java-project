public class Book
{
    public String author;
    public String ISBN;
    public String title;
    public int year;
    public int amount;

    Book(String author, String ISBN, String title, int year, int amount)
    {
        this.author = author;
        this.ISBN = ISBN;
        this.title = title;
        this.year = year;
        this.amount = amount;
    }
}
