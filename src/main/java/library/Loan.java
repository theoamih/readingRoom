package library;

import library.enums.LoanStatus;

/**
 * One borrowing of one item by one member.
 *
 * <p>The constructor and {@link #markReturned()} are package-private, so the
 * compiler stops code outside the {@code library} package from creating or closing
 * a loan. Inside the package, only {@link Library} should call them. The compiler
 * doesn't enforce that part, so this package should stay small.
 */
public final class Loan {
    private final int id;
    private final Member member;
    private final LendingItem item;
    private final int loanDays;
    private LoanStatus status = LoanStatus.ACTIVE;

    Loan(int id, Member member, LendingItem item, int loanDays) {
        this.id = id;
        this.member = member;
        this.item = item;
        this.loanDays = loanDays;
    }

    void markReturned() {
        status = LoanStatus.RETURNED;
    }

    public boolean isActive() {
        return status == LoanStatus.ACTIVE;
    }

    public int getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LendingItem getItem() {
        return item;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public LoanStatus getStatus() {
        return status;
    }
}
