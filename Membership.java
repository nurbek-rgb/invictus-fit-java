import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Абстрактілі класс: "жалпы абонемент" жоқ, тек нақты түрлері бар
public abstract class Membership {
    private final String id;
    private final LocalDate startDate;
    protected final double price;

    protected Membership(String id, LocalDate startDate, double price) {
        this.id = id;
        this.startDate = startDate;
        this.price = price;
    }

    // Әр түрі өз мерзімін айтады (полиморфизм)
    public abstract int getDurationDays();

    public double calculatePrice() { return price; }

    public LocalDate getEndDate() { return startDate.plusDays(getDurationDays()); }

    public boolean isActive(LocalDate today) {
        return !today.isBefore(startDate) && !today.isAfter(getEndDate());
    }

    public long daysLeft(LocalDate today) {
        return ChronoUnit.DAYS.between(today, getEndDate());
    }

    // Мерзімі біте жаздады ма (мысалы, 7 күннен аз қалды)
    public boolean isExpiringSoon(LocalDate today, int warnDays) {
        return isActive(today) && daysLeft(today) <= warnDays;
    }

    // Клубқа кіру кезінде шақырылады. Әдетте true, сынақ абонементте кіру саны азаяды
    public boolean consumeVisit() { return true; }

    public String getId() { return id; }
}
