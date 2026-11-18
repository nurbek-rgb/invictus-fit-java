import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Schedule {
    private final Map<DayOfWeek, List<GroupClass>> classes = new EnumMap<>(DayOfWeek.class);

    public void addClass(DayOfWeek day, GroupClass c) {
        classes.computeIfAbsent(day, d -> new ArrayList<>()).add(c);
    }

    public List<GroupClass> getByDay(DayOfWeek day) {
        return classes.getOrDefault(day, new ArrayList<>());
    }
}
