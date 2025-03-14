import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

class LibraryTest
{
    @Mock
    Database db;

    @InjectMocks
    Library lib;

    LibraryTest() { MockitoAnnotations.openMocks(this); }

    @BeforeEach
    void init()
    {
        Member m = new Member();
        m.first_name = "fake";
        m.last_name = "user";
        m.id = "00000";
        m.level = 0;

        when(db.get_member("fake_user"))
            .thenReturn(m);

        lib = new Library("fake_user");
    }

    @Mock
    Database store;
    @Test
    void test_lend_book1()
    {

        Library cut = new Library(store, "jjjjjj");


    }

    @Test
    void test_lend_book()
    {
        when(db.get_loans("00000", ""))
                .thenReturn(new Loan[]{});
        when(db.get_loans("", "boken"))
                .thenReturn(new Loan[]{});

        assertFalse(lib.lend_book("boken"));

        Book b = new Book();
        b.author = "olof";
        b.title = "boken";
        b.ISBN = "boken";
        b.year = 2025;
        b.amount = 0;
        when(db.get_book("boken"))
                .thenReturn(b);
        assertTrue(lib.lend_book("boken"));
    }

    @Test
    void test_return_book()
    {

    }

    @Test
    void test_create_member()
    {

    }

    @Test
    void test_delete_member()
    {

    }

    @Test
    void test_suspend_member()
    {

    }
}