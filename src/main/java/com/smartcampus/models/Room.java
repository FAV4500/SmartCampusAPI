package com.smartcampus.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement // This helps the server understand how to turn the object back into JSON
public class Room {
    private String id;
    private String name;
    private int capacity;

    public Room() {} 

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}