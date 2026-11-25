import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.Test;

class ScheduleTest {
    @Test
    void scheduleKeepsDaysSeparateAndProtectsItsContents() {
        Schedule schedule = new Schedule();
        GroupClass yoga = new GroupClass("Yoga", ClassCategory.MIND_BODY, Level.EASY, 1, new Trainer("Алия", "Yoga"));
        schedule.addClass(DayOfWeek.MONDAY, yoga);
        assertEquals(List.of(yoga), schedule.getByDay(DayOfWeek.MONDAY));
        assertTrue(schedule.getByDay(DayOfWeek.TUESDAY).isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> schedule.getByDay(DayOfWeek.MONDAY).clear());
        assertThrows(NullPointerException.class, () -> schedule.addClass(DayOfWeek.MONDAY, null));
        assertThrows(NullPointerException.class, () -> schedule.addClass(null, yoga));
        assertEquals(List.of(yoga), schedule.getByDay(DayOfWeek.MONDAY));
    }
}
