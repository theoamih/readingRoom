package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A library's items, members and loans.
 *
 * <p>Every loan is created and closed here, so this is where the rule that an
 * item can only be on one loan at a time is enforced. See {@link Loan} for what
 * stops other code getting around it.
 */
public class Library {
    private final String name;
    private final List<LendingItem> items = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();

    private int nextLoanId = 1;

    public Library(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public void addItem(LendingItem item) {
        items.add(Objects.requireNonNull(item));
    }

    public void addMember(Member member) {
        members.add(Objects.requireNonNull(member));
    }

    /**
     * Lends an item to a member and prints a confirmation.
     *
     * <p>The check comes first, so a refused borrow changes nothing: no loan,
     * no history entry and no used loan ID. As in the original program, the item
     * and member don't have to have been added to this library.
     *
     * @param member the member borrowing the item
     * @param item the item to lend
     * @throws ItemAlreadyOnLoanException if the item is already out
     */
    public void borrow(Member member, LendingItem item) {
        Optional<Member> holder = currentBorrower(item);

        if (holder.isPresent()) {
            throw new ItemAlreadyOnLoanException(item, holder.get());
        }

        // The item's type decides the period, so Library needs no book or
        // audio-kit logic. A new item type doesn't change this method.
        int loanDays = item.getType().loanDaysFor(member.getKind());

        loans.add(new Loan(nextLoanId, member, item, loanDays));
        nextLoanId++;
        member.recordBorrow(item);
        System.out.println("Lent \"" + item.getTitle() + "\" to " + member.getName()
                + " for " + loanDays + " days");
    }

    /**
     * Closes the item's active loan, if it has one, and prints a confirmation.
     *
     * <p>As in the original program, returning an item that isn't out still
     * prints the confirmation.
     *
     * @param item the item being returned
     */
    public void returnItem(LendingItem item) {
        findActiveLoan(item).ifPresent(Loan::markReturned);
        System.out.println("Returned \"" + item.getTitle() + "\"");
    }

    /**
     * Finds who has an item. This is worked out from the loans each time rather
     * than stored on the item, so the two can never disagree.
     *
     * @param item the item to look up
     * @return the member who has the item, or empty if it isn't out
     */
    public Optional<Member> currentBorrower(LendingItem item) {
        return findActiveLoan(item).map(Loan::getMember);
    }

    private Optional<Loan> findActiveLoan(LendingItem item) {
        return loans.stream()
                .filter(Loan::isActive)
                .filter(loan -> loan.getItem() == item) // checking same object instead of title
                .findFirst();
    }

    public String getName() {
        return name;
    }

    // The next three lists are read-only views. Calling add() or clear() on one
    // throws an exception, so outside code can't change the library's records.
    public List<LendingItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

    /**
     * Returns every loan the library has made, including returned ones.
     *
     * @return a read-only view of the loans, oldest first
     */
    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    /**
     * Prints the end-of-day report: every item with its status, then every active loan.
     */
    public void printReport() {
        System.out.println();
        System.out.println("=== " + name + " - end of day ===");
        System.out.println("Items:");

        for (LendingItem item : items) {
            String status = currentBorrower(item)
                    .map(borrower -> "ON_LOAN - with " + borrower.getName())
                    .orElse("AVAILABLE");
            System.out.println("  \"" + item.getTitle() + "\" (" + item.getType().label() + "): " + status);
        }

        System.out.println("Active loans:");

        for (Loan loan : loans) {
            if (loan.isActive()) {
                System.out.println("  Loan #" + loan.getId() + ": \"" + loan.getItem().getTitle()
                        + "\" -> " + loan.getMember().getName());
            }
        }
    }
}
