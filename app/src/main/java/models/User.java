package models;

public class User {

    private String user_id;
    private String phone_number;
    private String email;
    private String admission_number;
    private String room;
    private String semester;
    private String hostel;
    private String block;
    private String name;
    private String department;

    public User(String user_id, String phone_number, String email, String admission_number, String room, String block, String hostel, String semester, String department, String name) {
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.email = email;
        this.admission_number = admission_number;
        this.room = room;
        this.semester = semester;
        this.hostel = hostel;
        this.block = block;
        this.name = name;
        this.department = department;
    }
    public User() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdmission_number() {
        return admission_number;
    }

    public void setAdmission_number(String admission_number) {
        this.admission_number = admission_number;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", admission_number='" + admission_number + '\'' +
                ", room='" + room + '\'' +
                ", semester='" + semester + '\'' +
                ", hostel='" + hostel + '\'' +
                ", block='" + block + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}

