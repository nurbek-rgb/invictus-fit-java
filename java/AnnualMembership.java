import java.time.LocalDate;

public class AnnualMembership extends Membership {
    private final int installments; // бөліп төлеу саны, мысалы 24

    public AnnualMembership(String id, LocalDate start, double price, int installments) {
        super(id, start, price);
        if (installments <= 0) throw new IllegalArgumentException("Төлем саны оң болуы керек");
        this.installments = installments;
    }

    @Override public int getDurationDays() { return 365; }

    public double getInstallmentAmount() { return price / installments; }
}
