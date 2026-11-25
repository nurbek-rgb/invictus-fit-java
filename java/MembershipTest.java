import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
        assertFalse(t.isActive(today));
    }

    @Test
    void membershipHasExactlyItsDeclaredNumberOfActiveDays() {
        for (Membership membership : new Membership[] {
                new MonthlyMembership("M", today, 25000),
                new AnnualMembership("A", today, 219000, 24),
                new TrialMembership("T", today, 2990)}) {
            assertFalse(membership.isActive(today.minusDays(1)));
            assertTrue(membership.isActive(today));
            assertTrue(membership.isActive(membership.getEndDate().minusDays(1)));
            assertFalse(membership.isActive(membership.getEndDate()));
            assertFalse(membership.isActive(membership.getEndDate().plusDays(1)));
        }
    }

    @Test
    void warningsIncludeThresholdButExcludeExpiredAndFutureMemberships() {
        Membership membership = new MonthlyMembership("M", today, 25000);
        assertFalse(membership.isExpiringSoon(today.minusDays(1), 40));
        assertFalse(membership.isExpiringSoon(today.plusDays(22), 7));
        assertTrue(membership.isExpiringSoon(today.plusDays(23), 7));
        assertTrue(membership.isExpiringSoon(today.plusDays(29), 7));
        assertFalse(membership.isExpiringSoon(today.plusDays(30), 7));
        assertThrows(IllegalArgumentException.class, () -> membership.isExpiringSoon(today, -1));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void invalidPricesAreRejected(double price) {
        assertThrows(IllegalArgumentException.class, () -> new MonthlyMembership("M", today, price));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void installmentCountMustBePositive(int installments) {
        assertThrows(IllegalArgumentException.class, () -> new AnnualMembership("A", today, 219000, installments));
    }

    @Test
    void missingMembershipIdentityOrDateIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new MonthlyMembership(" ", today, 25000));
        assertThrows(IllegalArgumentException.class, () -> new MonthlyMembership(null, today, 25000));
        assertThrows(NullPointerException.class, () -> new MonthlyMembership("M", null, 25000));
    }
}
