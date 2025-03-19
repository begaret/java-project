import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;

class LibraryTest
{
    @Mock
    Database db;
    Library lib;

    LibraryTest() { MockitoAnnotations.openMocks(this); }

    @BeforeEach
    void init()
    {
        when(db.get_member(0))
            .thenReturn(new Member("fake",
                    "user",
                    0,
                    Member.postgrad,
                    LocalDate.MIN));
        lib = new Library(db);
    }

    @Test
    void test_login()
    {
        assertTrue(lib.login(0));
    }

    @Test
    void test_lend_book()
    {
        assertTrue(lib.login(0));
        when(db.get_loans(0, ""))
                .thenReturn(new ArrayList<>());
        when(db.get_loans(-1, "boken"))
                .thenReturn(new ArrayList<>());

        assertFalse(lib.lend_book("boken"));

        when(db.get_book("boken"))
                .thenReturn(new Book("olof", "boken", "boken", 2025, 1));
        assertTrue(lib.lend_book("boken"));
    }

    @Test
    void test_return_book()
    {
        assertTrue(lib.login(0));

        when(db.get_book("boken"))
                .thenReturn(new Book("olof", "boken", "boken", 2025, 1));
        when(db.remove_loan(0, "boken"))
                .thenReturn(true);
        assertTrue(lib.return_book("boken"));
    }

    @Test
    void test_create_member()
    {
        assertTrue(lib.login(0));

        when(db.get_member(1))
                .thenReturn(new Member("evil", "user", 1, Member.student, LocalDate.MAX));
        assertFalse(lib.create_member(new Member("evil", "user", 1, Member.student, LocalDate.MIN)));
        when(db.get_member(2))
                .thenReturn(null);
        assertTrue(lib.create_member(new Member("good", "user", 2, Member.student, LocalDate.MIN)));
    }

    @Test
    void test_suspend_member()
    {
        assertTrue(lib.login(0));

        when(db.get_member(1))
                .thenReturn(null);
        assertFalse(lib.suspend_member(1));

        when(db.get_member(1))
                .thenReturn(new Member("evil", "user", 1, Member.student, LocalDate.MAX));
        assertTrue(lib.suspend_member(1));
    }
}