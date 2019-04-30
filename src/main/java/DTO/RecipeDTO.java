package DTO;

import java.sql.Date;

public class RecipeDTO {

    private int id;
    private String name;
    private Date registrationDate;
    private int storageTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /** Returns the storage time in number of months */
    public int getStorageTime() {
        return storageTime;
    }

    /** Sets the storage time in number of months */
    public void setStorageTime(int storageTime) {
        this.storageTime = storageTime;
    }

}
