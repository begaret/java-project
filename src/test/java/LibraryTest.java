import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.time.LocalDate;

class LibraryTest
{
    @Mock
    Database db;
    Library lib;

    LibraryTest() { MockitoAnnotations.openMocks(this); }

    @BeforeEach
    void init()
    {
        when(db.get_member("fake_user"))
            .thenReturn(new Member("fake",
                    "user",
                    "fake_user",
                    Member.postgrad,
                    LocalDate.MIN));
        lib = new Library(db);
    }

    @Test
    void test_login()
    {
        assertTrue(lib.login("fake_user"));
    }

    @Test
    void test_lend_book()
    {
        assertTrue(lib.login("fake_user"));
        when(db.get_loans("fake_user", ""))
                .thenReturn(new Loan[]{});
        when(db.get_loans("", "boken"))
                .thenReturn(new Loan[]{});

        assertFalse(lib.lend_book("boken"));

        when(db.get_book("boken"))
                .thenReturn(new Book("olof", "boken", "boken", 2025, 1));
        assertTrue(lib.lend_book("boken"));
    }

    @Test
    void test_return_book()
    {
        assertTrue(lib.login("fake_user"));

        when(db.get_book("boken"))
                .thenReturn(new Book("olof", "boken", "boken", 2025, 1));
        when(db.remove_loan("fake_user", "boken"))
                .thenReturn(true);
        assertTrue(lib.return_book("boken"));
    }

    @Test
    void test_create_member()
    {
        assertTrue(lib.login("fake_user"));

        when(db.get_member("evil_user"))
                .thenReturn(new Member("evil", "user", "evil_user", Member.student, LocalDate.MAX));
        assertFalse(lib.create_member(new Member("evil", "user", "evil_user", Member.student, LocalDate.MIN)));
        when(db.get_member("good_user"))
                .thenReturn(null);
        assertTrue(lib.create_member(new Member("good", "user", "good_user", Member.student, LocalDate.MIN)));
    }

    @Test
    void test_suspend_member()
    {
        assertTrue(lib.login("fake_user"));

        when(db.get_member("evil_user"))
                .thenReturn(null);
        assertFalse(lib.suspend_member("evil_user"));

        when(db.get_member("evil_user"))
                .thenReturn(new Member("evil", "user", "evil_user", Member.student, LocalDate.MAX));
        assertTrue(lib.suspend_member("evil_user"));
    }
}