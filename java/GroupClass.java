import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Objects;

public class GroupClass {
    private static final int WAITING_LIMIT = 5; // күту тізімінің шегі

    private final String name;
    private final ClassCategory category;
    private final Level level;
    private final int capacity;
    private final Trainer trainer;
    private final List<Member> enrolled = new ArrayList<>();
    private final Queue<Member> waitingList = new LinkedList<>(); // кезек: бірінші келген бірінші шығады

    public GroupClass(String name, ClassCategory category, Level level, int capacity, Trainer trainer) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Сабақ атауы бос болмауы керек");
        if (capacity <= 0) throw new IllegalArgumentException("Орын саны оң болуы керек");
        this.name = name;
        this.category = Objects.requireNonNull(category, "category");
        this.level = Objects.requireNonNull(level, "level");
        this.capacity = capacity;
        this.trainer = Objects.requireNonNull(trainer, "trainer");
    }

    public boolean isFull() { return enrolled.size() >= capacity; }

    // Мәселенің шешімі: сабақ толса, мүше бас тартпайды, кезекке тұрады
    public EnrollResult enroll(Member m, LocalDate today)
            throws MembershipExpiredException, ClassFullException {
        Objects.requireNonNull(m, "member");
        m.requireActiveMembership(today);                 // 1) абонемент жарамды ма
        if (enrolled.contains(m) || waitingList.contains(m)) return EnrollResult.ALREADY_IN;
        if (!isFull()) {                                  // 2) орын бар
            enrolled.add(m);
            return EnrollResult.ENROLLED;
        }
        if (waitingList.size() >= WAITING_LIMIT) throw new ClassFullException(name);
        waitingList.add(m);                               // 3) орын жоқ: кезекке
        return EnrollResult.WAITLISTED;
    }

    // Бос орын туралы хабарлауға болатын мүшелерді FIFO ретімен қайтарады.
    // Орын автоматты берілмейді: қайтарылған мүшелер қайта enroll() шақырады.
    public List<Member> cancel(Member m, LocalDate today) {
        Objects.requireNonNull(m, "member");
        Objects.requireNonNull(today, "today");
        if (!enrolled.remove(m)) {          // сабақта емес еді, кезектен алып тастаймыз
            waitingList.remove(m);
            return List.of();
        }
        List<Member> notified = new ArrayList<>();
        while (!waitingList.isEmpty()) {
            Member next = waitingList.poll();
            if (next.hasActiveMembership(today)) {  // абонементі өтіп кеткен болса, өткізіп жібереміз
                notified.add(next);
            }
        }
        return List.copyOf(notified);
    }

    public String getName() { return name; }
    public ClassCategory getCategory() { return category; }
    public Level getLevel() { return level; }
    public Trainer getTrainer() { return trainer; }
    public int getFreeSpots() { return capacity - enrolled.size(); }
    public List<Member> getEnrolled() { return List.copyOf(enrolled); }
    public Queue<Member> getWaitingList() { return new LinkedList<>(waitingList); }
}
