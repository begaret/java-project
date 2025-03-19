public class Book
{
    public String author;
    public String ISBN;
    public String title;
    public int year;

    Book() {}

    Book(String author, String ISBN, String title, int year)
    {
        this.author = author;
        this.ISBN = ISBN;
        this.title = title;
        this.year = year;
    }
}
