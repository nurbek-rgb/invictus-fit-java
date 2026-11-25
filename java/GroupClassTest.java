import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupClassTest {
    private final LocalDate today = LocalDate.of(2026, 11, 11);
    private GroupClass yoga;
    private Member first, second, expired;

    @BeforeEach
    void setUp() {
        yoga = new GroupClass("Yoga", ClassCategory.MIND_BODY, Level.EASY, 1, new Trainer("Алия", "Yoga"));
        first = memberWith(1, new MonthlyMembership("M1", today, 25000));
        second = memberWith(2, new MonthlyMembership("M2", today, 25000));
        expired = memberWith(3, new MonthlyMembership("M3", today.minusDays(60), 25000));
    }

    private Member memberWith(int id, Membership ms) {
        Member m = new Member(id, "Мүше" + id, "+7 700 000 00 0" + id);
        m.buyMembership(ms);
        return m;
    }

    @Test
    void fullClassPutsMemberOnWaitingList() throws Exception {
        assertEquals(EnrollResult.ENROLLED, yoga.enroll(first, today));
        assertEquals(EnrollResult.WAITLISTED, yoga.enroll(second, today));
        assertEquals(EnrollResult.ALREADY_IN, yoga.enroll(second, today));
    }

    @Test
    void expiredMembershipCannotEnroll() {
        assertThrows(MembershipExpiredException.class, () -> yoga.enroll(expired, today));
    }

    @Test
    void cancelNotifiesWaitingMemberWhoThenEnrollsHimself() throws Exception {
        yoga.enroll(first, today);
        yoga.enroll(second, today);

        List<Member> notified = yoga.cancel(first, today);

        assertEquals(List.of(second), notified);
        assertEquals(1, yoga.getFreeSpots());          // орын автоматты берілмеген, мүше өзі тіркелуі керек
        assertEquals(EnrollResult.ENROLLED, yoga.enroll(second, today));
        assertTrue(yoga.getWaitingList().isEmpty());
    }

    @Test
    void waitingListLimitIsEnforcedWithoutChangingEnrollment() throws Exception {
        yoga.enroll(first, today);
        for (int id = 10; id < 15; id++) {
            assertEquals(EnrollResult.WAITLISTED,
                    yoga.enroll(memberWith(id, new MonthlyMembership("M" + id, today, 25000)), today));
        }
        assertThrows(ClassFullException.class, () -> yoga.enroll(second, today));
        assertEquals(5, yoga.getWaitingList().size());
        assertEquals(List.of(first), yoga.getEnrolled());
        assertEquals(0, yoga.getFreeSpots());
    }

    @Test
    void cancellationFiltersExpiredAndExhaustedWaitersAndPreservesOrder() throws Exception {
        Member ending = memberWith(3, new MonthlyMembership("M3", today.minusDays(29), 25000));
        TrialMembership trial = new TrialMembership("T", today, 2990);
        Member trialMember = memberWith(4, trial);
        Member last = memberWith(5, new MonthlyMembership("M5", today, 25000));
        yoga.enroll(first, today);
        for (Member member : List.of(ending, second, trialMember, last)) yoga.enroll(member, today);
        for (int i = 0; i < 3; i++) trialMember.checkIn(today.atTime(10 + i, 0), null);

        assertEquals(List.of(second, last), yoga.cancel(first, today.plusDays(1)));
        assertTrue(yoga.getEnrolled().isEmpty());
        assertTrue(yoga.getWaitingList().isEmpty());
        assertEquals(EnrollResult.ENROLLED, yoga.enroll(second, today.plusDays(1)));
        assertEquals(EnrollResult.WAITLISTED, yoga.enroll(last, today.plusDays(1)));
    }

    @Test
    void cancellingWaiterOrUnknownMemberDoesNotReleaseOccupiedSeat() throws Exception {
        yoga.enroll(first, today);
        yoga.enroll(second, today);
        assertTrue(yoga.cancel(expired, today).isEmpty());
        assertEquals(1, yoga.getWaitingList().size());
        assertTrue(yoga.cancel(second, today).isEmpty());
        assertTrue(yoga.getWaitingList().isEmpty());
        assertEquals(List.of(first), yoga.getEnrolled());
        assertEquals(0, yoga.getFreeSpots());
    }

    @Test
    void duplicateMemberIdCannotOccupyAnotherSeatOrWaitlistPosition() throws Exception {
        yoga.enroll(first, today);
        Member copy = memberWith(first.getId(), new MonthlyMembership("COPY", today, 25000));
        assertEquals(EnrollResult.ALREADY_IN, yoga.enroll(copy, today));
        assertTrue(yoga.getWaitingList().isEmpty());
        yoga.enroll(second, today);
        Member secondCopy = memberWith(second.getId(), new MonthlyMembership("COPY2", today, 25000));
        assertEquals(EnrollResult.ALREADY_IN, yoga.enroll(secondCopy, today));
        assertEquals(1, yoga.getWaitingList().size());
    }

    @Test
    void callersCannotModifyEnrollmentThroughGetters() throws Exception {
        yoga.enroll(first, today);
        yoga.enroll(second, today);
        assertThrows(UnsupportedOperationException.class, () -> yoga.getEnrolled().clear());
        yoga.getWaitingList().clear();
        assertEquals(List.of(second), yoga.cancel(first, today));
    }

    @Test
    void exhaustedTrialCannotEnroll() {
        TrialMembership trial = new TrialMembership("T", today, 2990);
        Member member = memberWith(4, trial);
        for (int i = 0; i < 3; i++) trial.consumeVisit();
        assertThrows(MembershipExpiredException.class, () -> yoga.enroll(member, today));
        assertEquals(1, yoga.getFreeSpots());
    }

    @Test
    void capacityMustBePositive() {
        for (int capacity : new int[] {0, -1}) {
            assertThrows(IllegalArgumentException.class, () ->
                    new GroupClass("Yoga", ClassCategory.MIND_BODY, Level.EASY, capacity, new Trainer("Алия", "Yoga")));
        }
    }
}
