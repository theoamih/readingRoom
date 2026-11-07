package library;

import java.util.Objects;
import library.interfaces.ItemType;

/**
 * Something the library lends.
 *
 * <p>There's deliberately no status or borrower here. {@link Library} works those
 * out from the loans, so there's no second record to go wrong.
 */
public final class LendingItem {
    private final String title;
    private final ItemType type;

    public LendingItem(String title, ItemType type) {
        this.title = Objects.requireNonNull(title);
        this.type = Objects.requireNonNull(type);
    }

    /**
     * Returns the item's title. Two copies of a book can share a title, so don't
     * use this to tell items apart.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    public ItemType getType() {
        return type;
    }
}
