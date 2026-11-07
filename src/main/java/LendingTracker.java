// The Reading Room - community library lending tracker
// Runs a sample afternoon of lending and prints the end-of-day report.

import library.ItemAlreadyOnLoanException;
import library.LendingItem;
import library.Library;
import library.Member;
import library.enums.MemberKind;
import library.itemtypes.AudioKit;
import library.itemtypes.Book;

public class LendingTracker {

    public static void main(String[] args) {
        Library lib = new Library("The Reading Room");

        LendingItem novel = new LendingItem("Things Fall Apart", new Book());
        LendingItem audioCourse = new LendingItem("French Audio Course", new AudioKit());
        lib.addItem(novel);
        lib.addItem(audioCourse);

        Member kofi = new Member("Kofi Mensah", MemberKind.STANDARD);
        Member ama = new Member("Ama Owusu", MemberKind.STUDENT);
        lib.addMember(kofi);
        lib.addMember(ama);

        // a normal afternoon
        lib.borrow(kofi, novel);
        lib.borrow(ama, audioCourse);
        lib.returnItem(audioCourse);

        // The novel is already out with Kofi, so this is now refused.
        // We catch the exception here so the end-of-day report still prints.
        try {
            lib.borrow(ama, novel);
        } catch (ItemAlreadyOnLoanException refused) {
            System.out.println(refused.getMessage());
        }

        lib.printReport();
    }
}
