package by.skarulskaya.finalproject.model.entity;

public class User extends CustomEntity {
    public enum Role {
        ADMIN, CUSTOMER, GUEST
    }
    public enum Status {
        ACTIVE, BLOCKED, IN_REGISTRATION_PROCESS, DELETED
    }
    private String email;
    private String password;
    private String name;
    private String surname;
    private Role role;
    private Status status;

    public User(String email, String password, String name, String surname, Role role, Status status) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.status = status;
    }

    public User(int id, String email, String password, String name, String surname, Role role, Status status) {
        super(id);
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id='").append(id).append('\'');
        sb.append("email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", role=").append(role);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null) return false;
        if (o.getClass() != getClass()) return false;

        User user = (User) o;

        if (!email.equals(user.email)) return false; //todo is that correct without null check?
        if (!password.equals(user.password)) return false;
        if (!name.equals(user.name)) return false;
        if (!surname.equals(user.surname)) return false;
        if (role != user.role) return false;
        return status == user.status;
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
