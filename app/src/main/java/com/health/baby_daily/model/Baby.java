package com.health.baby_daily.model;

public class Baby {
    private String bId;
    private String fullName;
    private String image;
    private String gender;
    private String dob;
    private String height;
    private String weight;
    private String headC;
    private String created;
    private String modified;
    private String parent_id;
    private String caregiver_id;
    private String doctor_id;

    public Baby() {

    }

    public Baby (String b, String fN, String i, String g, String d, String h, String w, String hC, String c, String m, String pId, String cid, String did){
        bId = b;
        fullName = fN;
        image = i;
        gender = g;
        dob = d;
        height = h;
        weight = w;
        headC = hC;
        created = c;
        modified = m;
        parent_id = pId;
        caregiver_id = cid;
        doctor_id = did;
    }

    public String getbId() { return bId; }

    public void setbId(String bId) { this.bId = bId; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return dob; }

    public void setDob(String dob) { this.dob = dob; }

    public String getHeight() { return height; }

    public void setHeight(String height) { this.height = height; }

    public String getWeight() { return weight; }

    public void setWeight(String weight) { this.weight = weight; }

    public String getHeadC() { return headC; }

    public void setHeadC(String headC) { this.headC = headC; }

    public String getCreated() { return created; }

    public void setCreated(String created) { this.created = created; }

    public String getModified() { return modified; }

    public void setModified(String modified) { this.modified = modified; }

    public String getParent_id() { return parent_id; }

    public void setParent_id(String parent_id) { this.parent_id = parent_id; }

    public String getCaregiver_id() {
        return caregiver_id;
    }

    public void setCaregiver_id(String caregiver_id) {
        this.caregiver_id = caregiver_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }
}
