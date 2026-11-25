// Абонемент жоқ немесе мерзімі өткенде лақтырылады
public class MembershipExpiredException extends Exception {
    public MembershipExpiredException(String memberName) {
        super("Абонемент жарамсыз: " + memberName);
    }
}
