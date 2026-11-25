import java.time.LocalDate;

public class MonthlyMembership extends Membership {
    public MonthlyMembership(String id, LocalDate start, double price) {
        super(id, start, price);
    }

    @Override public int getDurationDays() { return 30; }
}
