import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReportService {
    public List<String> expiringWarnings(List<Member> members, LocalDate today, int warnDays) {
        Objects.requireNonNull(members, "members");
        Objects.requireNonNull(today, "today");
        if (warnDays < 0) throw new IllegalArgumentException("Ескерту күндері теріс болмауы керек");
        List<String> warnings = new ArrayList<>();
        for (Member member : members) {
            String warning = member.expiryWarning(today, warnDays);
            if (warning != null) warnings.add(warning);
        }
        return List.copyOf(warnings);
    }

    // Мүшелердің ағымдағы абонемент құны; нақты төлемдер тарихы емес.
    public Map<String, Double> revenueByType(List<Member> members) {
        Objects.requireNonNull(members, "members");
        Map<String, Double> revenue = new LinkedHashMap<>();
        for (Member member : members) {
            Membership membership = member.getMembership();
            if (membership != null) {
                revenue.merge(membership.getClass().getSimpleName(), membership.calculatePrice(), Double::sum);
            }
        }
        return Collections.unmodifiableMap(revenue);
    }
}
