package context;

public class PersonContext {
    private final ThreadLocal<String> firstName = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<String> lastName = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<String> emailAddress = ThreadLocal.withInitial(() -> null);

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmailAddress() {
        return emailAddress.get();
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress.set(emailAddress);
    }
}
