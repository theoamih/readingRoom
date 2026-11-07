package library;

import java.util.*;
import library.enums.MemberKind;

/**
 * A person who can borrow from the library.
 */
public final class Member {
    private static int nextId = 1;

    private final int id;
    private final String name;
    private final MemberKind kind;
    private final List<String> history = new ArrayList<>();

    public Member(String name, MemberKind kind) {
        this.id = nextId++;
        this.name = Objects.requireNonNull(name);
        this.kind = Objects.requireNonNull(kind);
    }

    /**
     * Adds a borrow to this member's history. Package-private so that code outside
     * the {@code library} package can't change it. Inside the package, only
     * {@link Library} should call this, though the compiler doesn't enforce that.
     *
     * @param item the item that was borrowed
     */
    void recordBorrow(LendingItem item) {
        history.add("Borrowed " + item.getTitle());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MemberKind getKind() {
        return kind;
    }

    /**
     * Returns what this member has borrowed. As in the original program, returns
     * are not recorded.
     *
     * @return a read-only view of the history, oldest first
     */
    public List<String> getHistory() {
        return Collections.unmodifiableList(history);
    }
}
