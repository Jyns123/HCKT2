package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private Set<GroupP> groups = new HashSet<>();

    public Person() {
    }

    public Set<GroupP> getGroups() {
        return groups;
    }
    public void setGroups(Set<GroupP> groups) {
        this.groups = groups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person(Long id, String name, Set<GroupP> groups) {
        this.id = id;
        this.name = name;
        this.groups = groups;
    }
}
