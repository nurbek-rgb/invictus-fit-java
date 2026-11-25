import java.time.LocalDateTime;
import java.util.Objects;

public class Visit {
    private final Member member;
    private final LocalDateTime dateTime;
    private final GroupClass groupClass; // жай кіру болса null

    public Visit(Member member, LocalDateTime dateTime, GroupClass groupClass) {
        this.member = Objects.requireNonNull(member, "member");
        this.dateTime = Objects.requireNonNull(dateTime, "dateTime");
        this.groupClass = groupClass;
    }

    public Member getMember() { return member; }
    public LocalDateTime getDateTime() { return dateTime; }
    public GroupClass getGroupClass() { return groupClass; }
}
