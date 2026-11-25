import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class MemberTest {
    private final LocalDate today = LocalDate.of(2026, 11, 11);

    @Test
    void checkInRecordsOnlyThreeSuccessfulTrialVisits() throws Exception {
        Member member = new Member(1, "Айдос", "+7 700 111 11 11");
        TrialMembership trial = new TrialMembership("T", today, 2990);
        member.buyMembership(trial);
        for (int i = 0; i < 3; i++) member.checkIn(today.atTime(10 + i, 0), null);

        assertThrows(MembershipExpiredException.class, () -> member.checkIn(today.atTime(14, 0), null));
        assertEquals(3, member.getVisits().size());
        assertEquals(0, trial.getVisitsLeft());
        assertFalse(member.hasActiveMembership(today));
        assertEquals(today.atTime(10, 0), member.getVisits().get(0).getDateTime());
        assertSame(member, member.getVisits().get(0).getMember());
        assertNull(member.getVisits().get(0).getGroupClass());
        assertThrows(UnsupportedOperationException.class, () -> member.getVisits().clear());
    }

    @Test
    void failedClassCheckInDoesNotConsumeTrialVisit() throws Exception {
        Member member = new Member(1, "Айдос", "+7 700 111 11 11");
        TrialMembership trial = new TrialMembership("T", today, 2990);
        member.buyMembership(trial);
        GroupClass yoga = new GroupClass("Yoga", ClassCategory.MIND_BODY, Level.EASY, 1, new Trainer("Алия", "Yoga"));
        assertThrows(IllegalStateException.class, () -> member.checkIn(today.atTime(10, 0), yoga));
        assertEquals(3, trial.getVisitsLeft());
        assertTrue(member.getVisits().isEmpty());

        yoga.enroll(member, today);
        assertEquals(3, trial.getVisitsLeft());
        member.checkIn(today.atTime(10, 0), yoga);
        assertEquals(2, trial.getVisitsLeft());
        assertSame(yoga, member.getVisits().get(0).getGroupClass());
    }

    @Test
    void waitlistedMemberCannotCheckInToClass() throws Exception {
        GroupClass yoga = new GroupClass("Yoga", ClassCategory.MIND_BODY, Level.EASY, 1, new Trainer("Алия", "Yoga"));
        Member first = new Member(1, "Айдос", "111");
        Member second = new Member(2, "Томирис", "222");
        first.buyMembership(new MonthlyMembership("M", today, 25000));
        TrialMembership trial = new TrialMembership("T", today, 2990);
        second.buyMembership(trial);
        yoga.enroll(first, today);
        yoga.enroll(second, today);
        assertThrows(IllegalStateException.class, () -> second.checkIn(today.atTime(10, 0), yoga));
        assertEquals(3, trial.getVisitsLeft());
        assertTrue(second.getVisits().isEmpty());
    }

    @Test
    void absentFutureAndExpiredMembershipCannotCheckIn() {
        Member member = new Member(1, "Айдос", "111");
        assertThrows(MembershipExpiredException.class, () -> member.checkIn(today.atStartOfDay(), null));
        for (LocalDate start : new LocalDate[] {today.plusDays(1), today.minusDays(14)}) {
            TrialMembership trial = new TrialMembership("T", start, 2990);
            member.buyMembership(trial);
            assertThrows(MembershipExpiredException.class, () -> member.checkIn(today.atStartOfDay(), null));
            assertEquals(3, trial.getVisitsLeft());
        }
        assertTrue(member.getVisits().isEmpty());
    }
}
