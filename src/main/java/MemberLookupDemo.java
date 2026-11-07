import java.util.List;
import library.LendingItem;
import library.Library;
import library.Loan;
import library.Member;
import library.enums.MemberKind;
import library.itemtypes.AudioKit;
import library.itemtypes.Book;

/**
 * Shows what {@link Library#activeLoansFor(Member)} gives a volunteer. It's separate
 * from {@code LendingTracker} so that program's output stays the same as the original.
 */
public class MemberLookupDemo {

    public static void main(String[] args) {
        Library lib = new Library("The Reading Room");

        LendingItem novel = new LendingItem("Things Fall Apart", new Book());
        LendingItem audioCourse = new LendingItem("French Audio Course", new AudioKit());
        Member kofi = new Member("Kofi Mensah", MemberKind.STANDARD);
        Member ama = new Member("Ama Owusu", MemberKind.STUDENT);

        lib.borrow(kofi, novel);
        lib.borrow(ama, audioCourse);
        printLoans(lib, kofi);
        printLoans(lib, ama);

        lib.returnItem(audioCourse);
        printLoans(lib, ama);
    }

    private static void printLoans(Library lib, Member member) {
        List<Loan> loans = lib.activeLoansFor(member);
        System.out.println(member.getName() + " has " + loans.size() + " item(s) out:");
        for (Loan loan : loans) {
            System.out.println("  \"" + loan.getItem().getTitle() + "\" for " + loan.getLoanDays() + " days");
        }
    }
}
