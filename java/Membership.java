import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

// Абстрактілі класс: "жалпы абонемент" жоқ, тек нақты түрлері бар
public abstract class Membership {
    private final String id;
    private final LocalDate startDate;
    protected final double price;

    protected Membership(String id, LocalDate startDate, double price) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Абонемент ID бос болмауы керек");
        if (!Double.isFinite(price) || price < 0) throw new IllegalArgumentException("Баға теріс емес, шекті сан болуы керек");
        this.id = id;
        this.startDate = Objects.requireNonNull(startDate, "startDate");
        this.price = price;
    }

    // Әр түрі өз мерзімін айтады (полиморфизм)
    public abstract int getDurationDays();

    public double calculatePrice() { return price; }

    // Аяқталу күні — абонемент жарамсыз болатын алғашқы күн: [startDate, endDate).
    public LocalDate getEndDate() { return startDate.plusDays(getDurationDays()); }

    public boolean isActive(LocalDate today) {
        Objects.requireNonNull(today, "today");
        return !today.isBefore(startDate) && today.isBefore(getEndDate());
    }

    public long daysLeft(LocalDate today) {
        return ChronoUnit.DAYS.between(today, getEndDate());
    }

    // Мерзімі біте жаздады ма (мысалы, 7 күннен аз қалды)
    public boolean isExpiringSoon(LocalDate today, int warnDays) {
        if (warnDays < 0) throw new IllegalArgumentException("Ескерту күндері теріс болмауы керек");
        return isActive(today) && daysLeft(today) <= warnDays;
    }

    // Клубқа кіру кезінде шақырылады. Әдетте true, сынақ абонементте кіру саны азаяды
    public boolean consumeVisit() { return true; }

    public String getId() { return id; }
    public LocalDate getStartDate() { return startDate; }
}
