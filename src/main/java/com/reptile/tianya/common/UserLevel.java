package com.reptile.tianya.common;


public enum UserLevel {
    RED_1("b_red_1.gif", 1), RED_2("b_red_2.gif", 2), RED_3("b_red_3.gif", 3), RED_4("b_red_4.gif", 4), RED_5("b_red_5.gif", 5),

    BLUE_1("b_blue_1.gif", 6), BLUE_2("b_blue_2.gif", 7), BLUE_3("b_blue_3.gif", 8), BLUE_4("b_blue_4.gif", 9), BLUE_5("b_blue_5.gif", 10),

    CAP_1("b_cap_1.gif", 11), CAP_2("b_cap_2.gif", 12), CAP_3("b_cap_3.gif", 13), CAP_4("b_cap_4.gif", 14), CAP_5("b_cap_5.gif", 15),

    CROWN_1("b_crown_1.gif", 16), CROWN_2("b_crown_2.gif", 17), CROWN_3("b_crown_3.gif", 18), CROWN_4("b_crown_4.gif", 19), CROWN_5("b_crown_5.gif", 20);

    private String name;
    private int level;

    private UserLevel(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static UserLevel getByLevel(int v) {
        UserLevel level = null;
        if (RED_1.level == v) {
            level = RED_1;
        } else if (RED_2.level == v) {
            level = RED_2;
        } else if (RED_3.level == v) {
            level = RED_3;
        } else if (RED_4.level == v) {
            level = RED_4;
        } else if (RED_5.level == v) {
            level = RED_5;
        } else if (BLUE_1.level == v) {
            level = BLUE_1;
        } else if (BLUE_2.level == v) {
            level = BLUE_2;
        } else if (BLUE_3.level == v) {
            level = BLUE_3;
        } else if (BLUE_4.level == v) {
            level = BLUE_4;
        } else if (BLUE_5.level == v) {
            level = BLUE_5;
        } else if (CAP_1.level == v) {
            level = CAP_1;
        } else if (CAP_2.level == v) {
            level = CAP_2;
        } else if (CAP_3.level == v) {
            level = CAP_3;
        } else if (CAP_4.level == v) {
            level = CAP_4;
        } else if (CAP_5.level == v) {
            level = CAP_5;
        } else if (CROWN_1.level == v) {
            level = CROWN_1;
        } else if (CROWN_2.level == v) {
            level = CROWN_2;
        } else if (CROWN_3.level == v) {
            level = CROWN_3;
        } else if (CROWN_4.level == v) {
            level = CROWN_4;
        } else if (CROWN_5.level == v) {
            level = CROWN_5;
        }
        return level;
    }

    public static UserLevel getByValue(String v) {
        UserLevel level = null;
        if (RED_1.name.contains(v)) {
            level = RED_1;
        } else if (RED_2.name.contains(v)) {
            level = RED_2;
        } else if (RED_3.name.contains(v)) {
            level = RED_3;
        } else if (RED_4.name.contains(v)) {
            level = RED_4;
        } else if (RED_5.name.contains(v)) {
            level = RED_5;
        } else if (BLUE_1.name.contains(v)) {
            level = BLUE_1;
        } else if (BLUE_2.name.contains(v)) {
            level = BLUE_2;
        } else if (BLUE_3.name.contains(v)) {
            level = BLUE_3;
        } else if (BLUE_4.name.contains(v)) {
            level = BLUE_4;
        } else if (BLUE_5.name.contains(v)) {
            level = BLUE_5;
        } else if (CAP_1.name.contains(v)) {
            level = CAP_1;
        } else if (CAP_2.name.contains(v)) {
            level = CAP_2;
        } else if (CAP_3.name.contains(v)) {
            level = CAP_3;
        } else if (CAP_4.name.contains(v)) {
            level = CAP_4;
        } else if (CAP_5.name.contains(v)) {
            level = CAP_5;
        } else if (CROWN_1.name.contains(v)) {
            level = CROWN_1;
        } else if (CROWN_2.name.contains(v)) {
            level = CROWN_2;
        } else if (CROWN_3.name.contains(v)) {
            level = CROWN_3;
        } else if (CROWN_4.name.contains(v)) {
            level = CROWN_4;
        } else if (CROWN_5.name.contains(v)) {
            level = CROWN_5;
        }
        return level;
    }
}
