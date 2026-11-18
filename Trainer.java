public class Trainer {
    private final String fullName;
    private final String specialization;

    public Trainer(String fullName, String specialization) {
        this.fullName = fullName;
        this.specialization = specialization;
    }

    public String getFullName() { return fullName; }
    public String getSpecialization() { return specialization; }
}
