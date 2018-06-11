package com.rahnuma.rahnuma;

/**
 * Created by Sikandar on 4/23/2017.
 */



/**
 * Created by Sikandar on 4/18/2017.
 */

//public class weather {
//    public String icon;
//    public String title;
//    public weather(){
//        super();
//    }
//
//    public weather(String name, String pic) {
//        super();
//        this.icon = pic;
//        this.title =name;
//    }
//
//    public String get_name(){
//        return this.title;
//    }
//
//    public String get_pic(){
//        return this.icon;
//    }
//}

class new_personal {
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String firstname;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    String lastname;
    String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    String mobile;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String city;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    String pic;


public new_personal(String fname,String Lname,String username,String email,String mobile,String city,String pic){
    this.firstname = fname;
    this.firstname = Lname;
    this.email=email;
    this.mobile=mobile;
    this.city=city;
    this.pic=pic;
    this.username=username;
}

}