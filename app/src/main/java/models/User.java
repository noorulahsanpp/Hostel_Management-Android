package models;

public class User {

    private String user_id;
    private String phone_number;
    private String email;
    private String admission_number;

    public User(String user_id, String phone_number, String email, String admission_number)
    {
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.email = email;
        this.admission_number = admission_number;
    }
    public User(){

    }

    public String getUser_id(){
        return user_id;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }
    public String getPhone_number(){
        return phone_number;
    }
    public void setPhone_number(String phone_number){
        this.phone_number = phone_number;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getAdmission_number(){
        return admission_number;
    }
    public void setAdmission_number(String admission_number){
        this.admission_number = admission_number;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", admission_number='" + admission_number + '\'' +
                '}';
    }
}
