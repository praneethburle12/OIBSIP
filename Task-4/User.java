package onlineexamination;

public class User {
    private String username;
    private String password;
    private String fullName;
    private String email;

    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public void updateProfile(String fullName, String email) {
        if (fullName != null && !fullName.isEmpty())
            this.fullName = fullName;
        if (email != null && !email.isEmpty())
            this.email = email;
    }

    public void updatePassword(String newPassword) {
        if (newPassword != null && !newPassword.isEmpty())
            this.password = newPassword;
    }

    @Override
    public String toString() {
        return "User Profile [Username=" + username + ", Name=" + fullName + ", Email=" + email + "]";
    }
}
