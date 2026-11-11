// Сабақ та, күту тізімі де толы болғанда лақтырылады
public class ClassFullException extends Exception {
    public ClassFullException(String className) {
        super("Сабақ пен күту тізімі толы: " + className);
    }
}
