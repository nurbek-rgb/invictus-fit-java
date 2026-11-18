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
}
