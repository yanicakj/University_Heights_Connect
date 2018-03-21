package com.universityhillsocial.universityhillsocial.Share;

/**
 * Created by Kubie on 3/20/18.
 */

public class ClassHolder {

    String className;
    String professorName;
    String credits;
    String semester;

    public ClassHolder() {

    }

    public ClassHolder(String className, String professorName, String credits, String semester) {
        this.className = className;
        this.professorName = professorName;
        this.credits = credits;
        this.semester = semester;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
