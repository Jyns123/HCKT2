package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupPController {
    @Autowired
    private GroupPRepository groupPRepository;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public ResponseEntity<List<GroupP>> getAllGroups() {
        return ResponseEntity.ok(groupPRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupP> getGroupById(@PathVariable(value = "id") Long groupId) {
        return groupPRepository.findById(groupId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<List<Person>> getGroupsByPersonId(@PathVariable(value = "id") Long id) {
        Optional<GroupP> group = groupPRepository.findById(id);
        if(group.isPresent()){
            List<Person> persons = new ArrayList<>(group.get().getMembers());
            return new ResponseEntity<>(persons, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<String> GroupP(@RequestBody GroupP groupP) {
        groupPRepository.save(groupP);
        return ResponseEntity.status(201).body("Created");
    }

    @PostMapping("/{personID}/{groupID}")
    public ResponseEntity<String> addPersonToGroup(@PathVariable Long personId, @PathVariable Long groupId) {
        Optional<GroupP> groupOpt = groupPRepository.findById(groupId);
        Optional<Person> personOpt = personRepository.findById(personId);

        if (!groupOpt.isPresent() || !personOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group or Person not found");
        }

        GroupP group = groupOpt.get();
        Person person = personOpt.get();

        group.getMembers().add(person);
        groupPRepository.save(group);

        return ResponseEntity.status(HttpStatus.OK).body("Person added to Group");
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable(value = "id") Long groupId) {
        groupPRepository.deleteById(groupId);
        return ResponseEntity.status(200).body("Deleted");
    }
    

    

}

