import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        this.name = name;
        this.category = category;
        this.level = level;
        this.capacity = capacity;
        this.trainer = trainer;
    }

    public boolean isFull() { return enrolled.size() >= capacity; }

    // Мәселенің шешімі: сабақ толса, мүше бас тартпайды, кезекке тұрады
    public EnrollResult enroll(Member m, LocalDate today)
            throws MembershipExpiredException, ClassFullException {
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

    // Мүше бас тартса, орын кезектегі келесі мүшеге беріледі. Жаңа жазылған мүшені қайтарады
    public Member cancel(Member m, LocalDate today) {
        if (!enrolled.remove(m)) {          // сабақта емес еді, кезектен алып тастаймыз
            waitingList.remove(m);
            return null;
        }
        while (!waitingList.isEmpty()) {
            Member next = waitingList.poll();
            if (next.hasActiveMembership(today)) {  // абонементі өтіп кеткен болса, өткізіп жібереміз
                enrolled.add(next);
                return next;
            }
        }
        return null;
    }

    public String getName() { return name; }
    public ClassCategory getCategory() { return category; }
    public Level getLevel() { return level; }
    public Trainer getTrainer() { return trainer; }
    public int getFreeSpots() { return capacity - enrolled.size(); }
    public List<Member> getEnrolled() { return enrolled; }
    public Queue<Member> getWaitingList() { return waitingList; }
}
