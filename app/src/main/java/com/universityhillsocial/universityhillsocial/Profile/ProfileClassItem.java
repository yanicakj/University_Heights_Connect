package com.universityhillsocial.universityhillsocial.Profile;

/**
 * Created by Kubie on 3/31/18.
 */

public class ProfileClassItem {

    String classname, professorname, semester;

    public ProfileClassItem() {
    }

    public ProfileClassItem(String classname, String professorname, String semester) {
        this.classname = classname;
        this.professorname = professorname;
        this.semester = semester;

    }

    public String getProfessorname() {
        return professorname;
    }

    public void setProfessorname(String professorname) {
        this.professorname = professorname;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }
}
