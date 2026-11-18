import java.time.LocalDateTime;

public class Visit {
    private final Member member;
    private final LocalDateTime dateTime;
    private final GroupClass groupClass; // жай кіру болса null

    public Visit(Member member, LocalDateTime dateTime, GroupClass groupClass) {
        this.member = member;
        this.dateTime = dateTime;
        this.groupClass = groupClass;
    }

    public Member getMember() { return member; }
    public LocalDateTime getDateTime() { return dateTime; }
    public GroupClass getGroupClass() { return groupClass; }
}
