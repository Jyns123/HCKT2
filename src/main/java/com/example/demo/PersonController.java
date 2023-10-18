package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private GroupPRepository grouppRepository;

    @GetMapping
    public ResponseEntity<List<Person>> persons(){
        List<Person> persons = personRepository.findAll();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> person(@PathVariable Long id){
        Optional<Person> person = personRepository.findById(id);
        if(person.isPresent()){
            return new ResponseEntity<>(person.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<List<GroupP>> groups(@PathVariable Long id){
        Optional<Person> person = personRepository.findById(id);
        if(person.isPresent()){
            List<GroupP> groupsList = new ArrayList<>(person.get().getGroups());
            return new ResponseEntity<>(groupsList, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<String> Person(@RequestBody Person person) {
        personRepository.save(person);
        return ResponseEntity.status(201).body("Created");
    }

    @PostMapping("/{groupId}/{personId}")
    public ResponseEntity<String> addPersonToGroup(@PathVariable Long groupId, @PathVariable Long personId) {
        Optional<GroupP> groupOpt = grouppRepository.findById(groupId);
        Optional<Person> personOpt = personRepository.findById(personId);

        if (!groupOpt.isPresent() || !personOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group or Person not found");
        }

        GroupP group = groupOpt.get();
        Person person = personOpt.get();

        group.getMembers().add(person);
        grouppRepository.save(group);

        return ResponseEntity.status(HttpStatus.OK).body("Person added to Group");
    }
}
