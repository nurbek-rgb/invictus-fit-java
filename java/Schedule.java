import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Schedule {
    private final Map<DayOfWeek, List<GroupClass>> classes = new EnumMap<>(DayOfWeek.class);

    public void addClass(DayOfWeek day, GroupClass c) {
        Objects.requireNonNull(day, "day");
        Objects.requireNonNull(c, "groupClass");
        classes.computeIfAbsent(day, d -> new ArrayList<>()).add(c);
    }

    public List<GroupClass> getByDay(DayOfWeek day) {
        Objects.requireNonNull(day, "day");
        return List.copyOf(classes.getOrDefault(day, List.of()));
    }
}
