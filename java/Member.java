import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Member {
    private final int id;
    private final String fullName;
    private final String phone;
    private Membership membership;                    // 0..1: абонементі болмауы да мүмкін
    private final List<Visit> visits = new ArrayList<>(); // *: көп келулер

    public Member(int id, String fullName, String phone) {
        if (id <= 0) throw new IllegalArgumentException("Мүше ID оң болуы керек");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Аты-жөні бос болмауы керек");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("Телефон бос болмауы керек");
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
    }

    public void buyMembership(Membership m) { this.membership = Objects.requireNonNull(m, "membership"); }

    public boolean hasActiveMembership(LocalDate today) {
        return membership != null && membership.isActive(today);
    }

    public void requireActiveMembership(LocalDate today) throws MembershipExpiredException {
        if (!hasActiveMembership(today)) throw new MembershipExpiredException(fullName);
    }

    // Клубқа / сабаққа келуді тіркеу
    public void checkIn(LocalDateTime now, GroupClass groupClass) throws MembershipExpiredException {
        requireActiveMembership(now.toLocalDate());
        if (groupClass != null && !groupClass.getEnrolled().contains(this)) {
            throw new IllegalStateException("Алдымен сабаққа жазылу керек: " + groupClass.getName());
        }
        if (!membership.consumeVisit()) throw new MembershipExpiredException(fullName);
        visits.add(new Visit(this, now, groupClass));
    }

    // Абонемент біте жаздаса, ескерту мәтінін қайтарады (әйтпесе null)
    public String expiryWarning(LocalDate today, int warnDays) {
        if (membership != null && membership.isExpiringSoon(today, warnDays)) {
            return fullName + ": абонементі аяқталуға " + membership.daysLeft(today)
                    + " күн қалды (" + membership.getEndDate() + ")";
        }
        return null;
    }

    public String getFullName() { return fullName; }
    public int getId() { return id; }
    public String getPhone() { return phone; }
    public Membership getMembership() { return membership; }
    public List<Visit> getVisits() { return List.copyOf(visits); }

    @Override public boolean equals(Object other) {
        return this == other || (other instanceof Member member && id == member.id);
    }

    @Override public int hashCode() { return Integer.hashCode(id); }
}
