import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ToIntFunction;

// Деректер бағдарлама жұмыс істеп тұрған кезде ғана сақталады.
public class InMemoryRepository<T> implements Repository<T> {
    private final Map<Integer, T> entities = new LinkedHashMap<>();
    private final ToIntFunction<T> idExtractor;

    public InMemoryRepository(ToIntFunction<T> idExtractor) {
        this.idExtractor = Objects.requireNonNull(idExtractor, "idExtractor");
    }

    @Override public void save(T entity) {
        Objects.requireNonNull(entity, "entity");
        entities.put(idExtractor.applyAsInt(entity), entity);
    }

    @Override public Optional<T> findById(int id) { return Optional.ofNullable(entities.get(id)); }
    @Override public List<T> findAll() { return List.copyOf(entities.values()); }
    @Override public void delete(int id) { entities.remove(id); }
}
