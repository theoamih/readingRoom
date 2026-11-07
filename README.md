# The Reading Room: Lending Tracker

Tracks what The Reading Room has lent out. It has two kinds of item (books and audio kits) and two kinds of member (standard and student). Students get longer loans.

The one rule that must never break: **an item can only be on one loan at a time.** `Library.borrow` enforces it and refuses a second loan.

The problems in the original program are written up in [DIAGNOSIS.md](DIAGNOSIS.md).

## Running it

You need JDK 21. There are no external dependencies.

With Maven:

```bash
mvn compile
mvn exec:java -Dexec.mainClass=LendingTracker
```

Or with the JDK directly:

```bash
javac -d target/classes -sourcepath src/main/java src/main/java/LendingTracker.java src/main/java/MemberLookupDemo.java
java -cp target/classes LendingTracker
```

To see the member lookup, run `MemberLookupDemo` the same way.

## Where things live

```
src/main/java/
  LendingTracker.java      the sample afternoon and end-of-day report
  MemberLookupDemo.java    shows the member lookup
  library/
    Library.java           lending, returns, the report and the one-loan rule
    Loan.java              one member borrowing one item
    LendingItem.java       something the library lends
    Member.java            someone who borrows
    ItemAlreadyOnLoanException.java
    enums/                 MemberKind, LoanStatus
    interfaces/            ItemType
    itemtypes/             Book, AudioKit
```

Keep `Library`, `Loan` and `Member` in the same package. `Library` uses code in the other two that isn't public, and that's what stops other code creating or closing a loan.

## Adding a new kind of item

Add one class to `itemtypes` that implements `ItemType`. The numbers here are only an example:

```java
public final class ELoan implements ItemType {

    @Override
    public String label() {
        return "E_LOAN";
    }

    @Override
    public int loanDaysFor(MemberKind kind) {
        return switch (kind) {
            case STANDARD -> 3;
            case STUDENT -> 7;
        };
    }
}
```

Then create items with `new LendingItem("Some title", new ELoan())`. `Library` and the report don't need to change, and the one-loan rule applies automatically.

## Design notes

### Status comes from the loans

The original stored who has an item on the item and again on the loan, and the two could disagree. `LendingItem` no longer has a status. `Library.currentBorrower` finds the item's active loan each time.

I could have kept the status field and updated it carefully, but that would leave two records to keep in step, which was the original problem.

### ItemType is an interface

An enum holding the loan periods would be shorter and would work for books and audio kits. I didn't use one because the brief says e-loans will have their own borrowing rules, which may be more than two numbers. Subclasses of `LendingItem` weren't needed either, because only the rules differ between items, not the item's data.

### Refusing a loan throws an exception

`borrow` could return `false`, but a caller could ignore that and carry on. The check runs before anything changes, so a refused borrow leaves no loan, no history entry and no used loan number. `main` catches the exception so the report still prints.

### Loan's constructor isn't public

The compiler stops code outside the `library` package from creating or closing a loan, so it can't skip the check in `borrow`. Inside the package it relies on people following the convention, so new item types go in `itemtypes`, which is outside it.

### Adding e-loans

In the old version, I'd have to add a new type string and a third branch in `borrow`, copying the status and print lines next to the book and audio-kit rules. That's the method every lending rule depends on.

Now it's one new class, and no existing file has to change beyond the code that creates the item.

This assumes e-loans follow the one-loan rule, because the brief states it for all items. If digital items can go to several people at once, or need a check other than a loan period, `ItemType` and `borrow` would have to change too.

### What changed from the sketch

- The sketch in `DIAGNOSIS.md` leaves out `ItemAlreadyOnLoanException`. I added it when I built the refusal.
- The classes are grouped into folders, which the sketch doesn't show.
- `Library` has a few methods the sketch doesn't show, such as `currentBorrower` and `activeLoansFor`.

## Behaviour compared with the original

`baseline-output.txt` is the original program's output, saved before any code changed. To compare, in PowerShell:

```powershell
javac -d target/classes -sourcepath src/main/java src/main/java/LendingTracker.java
java -cp target/classes LendingTracker | Out-File -Encoding ascii new-output.txt
git diff --no-index baseline-output.txt new-output.txt
```

Only the double loan changes:

```diff
-Lent "Things Fall Apart" to Ama Owusu for 28 days
+Cannot lend "Things Fall Apart": already on loan to Kofi Mensah
-  "Things Fall Apart" (BOOK): ON_LOAN - with Ama Owusu
+  "Things Fall Apart" (BOOK): ON_LOAN - with Kofi Mensah
-  Loan #3: "Things Fall Apart" -> Ama Owusu
```

These also behave differently, but the original program's `main` never reaches them:

- Unknown item types and member kinds no longer compile.
- Returning one of two same-titled copies closes only that copy's loan.
- Each `Library` numbers its own loans from 1.
- Null names, titles, types, kinds, items and members are rejected straight away.

These are kept the same as the original on purpose, because the brief only changes the one-loan rule:

- Returning an item that isn't out still prints `Returned`.
- An item or member that was never added to the library can still be used in a loan.
- History records borrows but not returns.

## One improvement: looking up a member

`Library.activeLoansFor(member)` lists what one member still has out.

When a volunteer is chasing a missing item, they want to know what that person has, and today that means reading through the whole report. The lookup uses the same loan records as the report, so the two can't disagree. It doesn't change the output of `LendingTracker`.

