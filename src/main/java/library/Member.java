package library;

import library.enums.MemberKind;

import java.util.*;

public class Member {
    public String name;
    public MemberKind kind; // "STANDARD" or "STUDENT"
    public int id;
    public List<String> history = new ArrayList<>();
    static int counter = 1;

    public Member(String name, MemberKind kind) {
        this.id = counter;
        counter++;
        this.name = name;
        this.kind = kind;
    }
}
