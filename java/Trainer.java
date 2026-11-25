public class Trainer {
    private final String fullName;
    private final String specialization;

    public Trainer(String fullName, String specialization) {
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Тренер аты бос болмауы керек");
        if (specialization == null || specialization.isBlank()) throw new IllegalArgumentException("Мамандық бос болмауы керек");
        this.fullName = fullName;
        this.specialization = specialization;
    }

    public String getFullName() { return fullName; }
    public String getSpecialization() { return specialization; }
}
