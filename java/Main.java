import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();

        // Тренер, сабақ (2 орын), кесте
        Trainer trainer = new Trainer("Алия", "Yoga");
        GroupClass yoga = new GroupClass("Yoga", ClassCategory.MIND_BODY, Level.EASY, 2, trainer);
        Schedule schedule = new Schedule();
        DayOfWeek day = today.getDayOfWeek();
        schedule.addClass(day, yoga);

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

        System.out.println("\n--- Бүгінгі кесте: " + today + " ---");
        for (GroupClass gc : schedule.getByDay(day)) {
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
        List<Member> notified = yoga.cancel(a, today);
        for (Member member : notified) {
            System.out.println("Бос орын туралы ескерту: " + member.getFullName());
        }
        System.out.println("Бос орын: " + yoga.getFreeSpots());

        System.out.println("\n--- Нұрбек қайта жазылып, сабаққа келді ---");
        try {
            System.out.println(c.getFullName() + " -> " + yoga.enroll(c, today));
            c.checkIn(today.atTime(18, 0), yoga);
            System.out.println("Trial: қалған кіру саны — " + ((TrialMembership) c.getMembership()).getVisitsLeft());
        } catch (MembershipExpiredException | ClassFullException ex) {
            System.out.println("ҚАТЕ: " + ex.getMessage());
        }

        Repository<Member> members = new InMemoryRepository<>(Member::getId);
        for (Member member : List.of(a, b, c, d, e)) members.save(member);
        ReportService report = new ReportService();
        System.out.println("\n--- Абонемент мерзімі ескертуі (7 күн) ---");
        report.expiringWarnings(members.findAll(), today, 7).forEach(System.out::println);
        System.out.println("\n--- Ағымдағы абонементтер құны (түрі бойынша) ---");
        report.revenueByType(members.findAll())
                .forEach((type, total) -> System.out.println(type + ": " + total + " ₸"));
    }
}
