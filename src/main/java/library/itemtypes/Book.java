package library.itemtypes;

import library.interfaces.ItemType;
import library.enums.MemberKind;

/**
 * A physical book: 14 days for standard members and 28 days for students.
 */
public final class Book implements ItemType {

    @Override
    public String label() {
        return "BOOK";
    }

    // There's no "default" case on purpose. If someone adds a new MemberKind,
    // this stops compiling until they decide what that member's loan period is.
    @Override
    public int loanDaysFor(MemberKind kind) {
        return switch (kind) {
            case STANDARD -> 14;
            case STUDENT -> 28;
        };
    }
}
