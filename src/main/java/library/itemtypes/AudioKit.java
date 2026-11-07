package library.itemtypes;

import library.interfaces.ItemType;
import library.enums.MemberKind;

/**
 * An audio kit: 7 days for standard members and 14 days for students.
 */
public final class AudioKit implements ItemType {

    @Override
    public String label() {
        return "AUDIO_KIT";
    }

    @Override
    public int loanDaysFor(MemberKind kind) {
        return switch (kind) {
            case STANDARD -> 7;
            case STUDENT -> 14;
        };
    }
}
