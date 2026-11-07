package library;

import java.io.Serial;

/**
 * Thrown when someone tries to borrow an item that is already out. The message
 * names the item and the member who has it.
 */
public class ItemAlreadyOnLoanException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    // Package-private: code outside this package can catch this but can't create one.
    ItemAlreadyOnLoanException(LendingItem item, Member holder) {
        super("Cannot lend \"" + item.getTitle() + "\": already on loan to " + holder.getName());
    }
}
