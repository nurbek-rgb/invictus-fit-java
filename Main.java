import java.time.DayOfWeek;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocalDate today = LocalDate.of(2026, 11, 11);

        // Тренер, сабақ (2 орын), кесте
        Trainer trainer = new Trainer("Алия", "Yoga");
        GroupClass yoga = new GroupClass("Yoga", ClassCategory.MIND_BODY, Level.EASY, 2, trainer);
        Schedule schedule = new Schedule();
        schedule.addClass(DayOfWeek.WEDNESDAY, yoga);

        // Мүшелер мен абонементтер (бағалар Invictus сайтынан)
        Member a = new Member(1, "Айдос", "+7 700 111 11 11");
        Member b = new Member(2, "Томирис", "+7 700 222 22 22");
        Member c = new Member(3, "Нұрбек", "+7 700 333 33 33");
        Member d = new Member(4, "Дана", "+7 700 444 44 44");
        Member e = new Member(5, "Ерлан", "+7 700 555 55 55");

        a.buyMembership(new AnnualMembership("A1", today.minusDays(10), 219000, 24));
        b.buyMembership(new AnnualMembership("A2", today.minusDays(20), 219000, 24));
        c.buyMembership(new TrialMembership("T1", today, 2990));
        d.buyMembership(new MonthlyMembership("M1", today.minusDays(60), 25000)); // мерзімі өткен
        e.buyMembership(new MonthlyMembership("M2", today.minusDays(25), 25000)); // 5 күн қалды

        AnnualMembership annual = (AnnualMembership) a.getMembership();
        System.out.println("Жылдық абонемент: " + annual.calculatePrice() + " ₸, айына: "
                + annual.getInstallmentAmount() + " ₸ (24 төлем)");

        System.out.println("\n--- Сәрсенбі кестесі ---");
        for (GroupClass gc : schedule.getByDay(DayOfWeek.WEDNESDAY)) {
            System.out.println(gc.getName() + " (" + gc.getCategory() + ", " + gc.getLevel()
                    + "), бос орын: " + gc.getFreeSpots());
        }

        System.out.println("\n--- Сабаққа жазылу ---");
        Member[] tries = {a, b, c, d};
        for (Member m : tries) {
            try {
                EnrollResult r = yoga.enroll(m, today);
                System.out.println(m.getFullName() + " -> " + r);
            } catch (MembershipExpiredException | ClassFullException ex) {
                System.out.println(m.getFullName() + " -> ҚАТЕ: " + ex.getMessage());
            }
        }

        System.out.println("\n--- Айдос бас тартты ---");
        Member promoted = yoga.cancel(a, today);
        if (promoted != null) {
            System.out.println("Кезектен сабаққа жазылды: " + promoted.getFullName());
        }
        System.out.println("Бос орын: " + yoga.getFreeSpots());

        System.out.println("\n--- Абонемент мерзімі ескертуі (7 күн) ---");
        Member[] all = {a, b, c, d, e};
        for (Member m : all) {
            String w = m.expiryWarning(today, 7);
            if (w != null) System.out.println(w);
        }
    }
}
