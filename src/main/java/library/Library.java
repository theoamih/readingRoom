package library;

import library.enums.ItemStatus;
import library.enums.ItemType;
import library.enums.LoanStatus;
import library.enums.MemberKind;

import java.util.*;

public class Library {
    public String name;
    public List<LendingItem> items = new ArrayList<>();
    public List<Member> members = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();

    public Library(String name) {
        this.name = name;
    }

    public void addItem(LendingItem lendingItem) {
        items.add(lendingItem);
    }

    public void addMember(Member member) {
        members.add(member);
    }

    // Lend an item to a member. The loan period depends on the kind of item and member.
    public void borrow(Member member, LendingItem lendingitem) {
        int loanDays = 0;
        if (lendingitem.type == ItemType.BOOK) {
            loanDays = 14;
            if (member.kind == MemberKind.STUDENT) {
                loanDays = 28; // students get longer loans
            }
            lendingitem.status = ItemStatus.ON_LOAN;
            lendingitem.currentBorrower = member.name;
            System.out.println("Lent \"" + lendingitem.title + "\" to " + member.name + " for " + loanDays + " days");
        } else if (lendingitem.type == ItemType.AUDIO_KIT) {
            loanDays = 7;
            if (member.kind == MemberKind.STUDENT) {
                loanDays = 14; // students get longer loans
            }
            lendingitem.status = ItemStatus.ON_LOAN;
            lendingitem.currentBorrower = member.name;
            System.out.println("Lent \"" + lendingitem.title + "\" to " + member.name + " for " + loanDays + " days");
        }
        member.history.add("Borrowed " + lendingitem.title);
        loans.add(new Loan(member.name, lendingitem.title, loanDays));
    }

    public void returnItem(LendingItem lendingitem) {
        lendingitem.status = ItemStatus.AVAILABLE;
        lendingitem.currentBorrower = null;
        for (Loan loan : loans) {
            if (loan.itemTitle.equals(lendingitem.title) && loan.status == LoanStatus.ACTIVE) {
                loan.status = LoanStatus.RETURNED;
            }
        }
        System.out.println("Returned \"" + lendingitem.title + "\"");
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void printReport() {
        System.out.println();
        System.out.println("=== " + name + " - end of day ===");
        System.out.println("Items:");
        for (LendingItem lendingItem : items) {
            System.out.println("  \"" + lendingItem.title + "\" (" + lendingItem.type + "): " + lendingItem.status
                    + (lendingItem.currentBorrower != null ? " - with " + lendingItem.currentBorrower : ""));
        }
        System.out.println("Active loans:");
        for (Loan loan : loans) {
            if (loan.status == LoanStatus.ACTIVE) {
                System.out.println("  Loan #" + loan.id + ": \"" + loan.itemTitle + "\" -> " + loan.memberName);
            }
        }
    }
}
