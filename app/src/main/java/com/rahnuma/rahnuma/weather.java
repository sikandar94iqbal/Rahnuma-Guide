package com.rahnuma.rahnuma;

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

class weather {
    String name;
    int price;
    String pic;
    boolean box;
String tourist_id;

    weather(String _describe, int _price, String _image, boolean _box,String tourist_id) {
        name = _describe;
        price = _price;
        this.tourist_id=tourist_id;
        this.pic = _image;
        box = _box;
    }

    public String get_name(){
        return  this.name;
    }

    public String get_pic(){
        return  this.pic;
    }

    public String get_tourist_id(){
        return  this.tourist_id;
    }
}