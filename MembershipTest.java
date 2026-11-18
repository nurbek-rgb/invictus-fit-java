import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class MembershipTest {
    private final LocalDate today = LocalDate.of(2026, 11, 11);

    @Test
    void expiredMonthlyMembershipIsNotActive() {
        Membership m = new MonthlyMembership("M1", today.minusDays(60), 25000);
        assertFalse(m.isActive(today));
    }

    @Test
    void polymorphismGivesDifferentDurations() {
        Membership monthly = new MonthlyMembership("M", today, 25000);
        Membership annual = new AnnualMembership("A", today, 219000, 24);
        assertEquals(30, monthly.getDurationDays());
        assertEquals(365, annual.getDurationDays());
        assertEquals(219000.0 / 24, ((AnnualMembership) annual).getInstallmentAmount(), 0.001);
    }

    @Test
    void trialMembershipAllowsOnlyThreeVisits() {
        TrialMembership t = new TrialMembership("T1", today, 2990);
        assertTrue(t.consumeVisit());
        assertTrue(t.consumeVisit());
        assertTrue(t.consumeVisit());
        assertFalse(t.consumeVisit());
        assertEquals(0, t.getVisitsLeft());
    }
}
