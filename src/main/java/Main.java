import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Library System!");
        System.out.println("Enter your user id:");
        int user = Integer.parseInt(scanner.nextLine());

        Database db = new Database("jdbc:sqlserver://172.27.129.59/SQLEXPRESS;database=libraryDB;password=123;");
        Library lib = new Library(db);
        if (!lib.login(user)) {
            System.out.println("Invalid user!");
            return;
        }

        System.out.println("\nMenu:");
        System.out.println("1. Lend item.");
        System.out.println("2. Return item.");
        System.out.println("3. Create user.");
        System.out.println("4. Delete user.");
        System.out.println("5. Suspend user.");
        System.out.println("6. Quit.");
        System.out.println("Select (1-6):");

        boolean quit = false;
        while (!quit) {
            int opt = Integer.parseInt(scanner.nextLine());
            switch (opt) {
            case 1: {
                System.out.println("Enter book ISBN: ");
                String ISBN = scanner.nextLine();
                lib.lend_book(ISBN);
            } break;
            case 2: {
                System.out.println("Enter book ISBN: ");
                String ISBN = scanner.nextLine();
                lib.return_book(ISBN);
            } break;

            case 3: {
                if (!lib.user_has_level(Member.librarian)) {
                    System.out.println("You are NOT a librarian!");
                    break;
                }

                Member member = new Member();
                System.out.println("Enter first name: ");
                member.first_name = scanner.nextLine();
                System.out.println("Enter first name: ");
                member.last_name = scanner.nextLine();
                System.out.println("Enter level: ");
                member.level = Integer.parseInt(scanner.nextLine());
                System.out.println("Enter id: ");
                member.id = Integer.parseInt(scanner.nextLine());
                lib.create_member(member);
            } break;

            case 4: {
                if (!lib.user_has_level(Member.librarian)) {
                    System.out.println("You are NOT a librarian!");
                    break;
                }

                System.out.println("Enter id: ");
                int id = Integer.parseInt(scanner.nextLine());
                lib.delete_member(id);
            } break;

            case 5: {
                if (!lib.user_has_level(Member.librarian)) {
                    System.out.println("You are NOT a librarian!");
                    break;
                }

                System.out.println("Enter id: ");
                int id = Integer.parseInt(scanner.nextLine());
                lib.suspend_member(id);
            } break;

            case 6: {
                quit = true;
            } break;
            }
        }

        db.shutdown();
    }
}
