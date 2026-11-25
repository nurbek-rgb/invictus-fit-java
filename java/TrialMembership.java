import java.time.LocalDate;

// Сынақ абонемент: 3 жаттығу (Invictus GO-да 2 990 ₸)
public class TrialMembership extends Membership {
    private int visitsLeft = 3;

    public TrialMembership(String id, LocalDate start, double price) {
        super(id, start, price);
    }

    @Override public int getDurationDays() { return 14; }

    @Override public boolean isActive(LocalDate today) {
        return super.isActive(today) && visitsLeft > 0;
    }

    @Override public boolean consumeVisit() {
        if (visitsLeft == 0) return false;
        visitsLeft--;
        return true;
    }

    public int getVisitsLeft() { return visitsLeft; }
}
