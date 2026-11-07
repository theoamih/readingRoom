package library.interfaces;

import library.enums.MemberKind;

/**
 * The rules that belong to one kind of lendable item.
 *
 * <p>To add a new kind of item, such as e-loans, write one new class that
 * implements this interface. {@code Library} and the report don't need to change.
 */
public interface ItemType {

    /**
     * Returns the name shown for this kind of item in the end-of-day report.
     *
     * @return the report label, such as {@code BOOK}
     */
    String label();

    /**
     * Returns how long this kind of item can be borrowed for.
     *
     * @param kind the kind of member borrowing the item
     * @return the loan period in days
     */
    int loanDaysFor(MemberKind kind);
}
