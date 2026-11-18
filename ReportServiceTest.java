import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ReportServiceTest {
    private final LocalDate today = LocalDate.of(2026, 11, 11);
    private final ReportService report = new ReportService();

    private Member memberWith(int id, Membership ms) {
        Member m = new Member(id, "Мүше" + id, "+7 700 000 00 0" + id);
        m.buyMembership(ms);
        return m;
    }

    @Test
    void expiringWarningsIncludesOnlyMembersEndingSoon() {
        Member soon = memberWith(1, new MonthlyMembership("M1", today.minusDays(25), 25000)); // 5 күн қалды
        Member later = memberWith(2, new AnnualMembership("A1", today, 219000, 24));
        assertEquals(1, report.expiringWarnings(List.of(soon, later), today, 7).size());
    }

    @Test
    void revenueIsGroupedByMembershipType() {
        Member a = memberWith(1, new AnnualMembership("A1", today, 219000, 24));
        Member b = memberWith(2, new AnnualMembership("A2", today, 219000, 24));
        Member c = memberWith(3, new MonthlyMembership("M1", today, 25000));
        Map<String, Double> revenue = report.revenueByType(List.of(a, b, c));
        assertEquals(438000.0, revenue.get("AnnualMembership"), 0.001);
        assertEquals(25000.0, revenue.get("MonthlyMembership"), 0.001);
    }

    @Test
    void inMemoryRepositoryStoresAndFindsByIdGenerically() {
        Repository<Member> repo = new InMemoryRepository<>(Member::getId);
        repo.save(memberWith(1, new MonthlyMembership("M1", today, 25000)));
        assertTrue(repo.findById(1).isPresent());
        assertEquals(1, repo.findAll().size());
        repo.delete(1);
        assertTrue(repo.findById(1).isEmpty());
    }
}
