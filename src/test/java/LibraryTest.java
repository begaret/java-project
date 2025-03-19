import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
        when(db.get_loans(0, "")).thenReturn(new ArrayList<>());
        when(db.get_loans(-1, "boken")).thenReturn(new ArrayList<>());
        when(db.add_loan(any(Loan.class))).thenReturn(true);

        when(db.get_book("boken"))
                .thenReturn(new Book("olof", "boken", "boken", 2025));
        assertTrue(lib.lend_book("boken"));

        verify(db).get_book("boken");
        verify(db).add_loan(any(Loan.class));
    }

    @Test
    void test_return_book()
    {
        assertTrue(lib.login(0));

        when(db.get_book("boken"))
                .thenReturn(new Book("olof", "boken", "boken", 2025));
        when(db.remove_loan(0, "boken"))
                .thenReturn(true);
        assertTrue(lib.return_book("boken"));

        verify(db).remove_loan(0, "boken");
    }

    @Test
    void test_create_member()
    {
        assertTrue(lib.login(0));

        when(db.get_member(2)).thenReturn(null);
        when(db.add_member(any(Member.class))).thenReturn(true);

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