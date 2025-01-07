package gevorgyan.pkmn.dao;

import lombok.RequiredArgsConstructor;
import gevorgyan.pkmn.entity.StudentEntity;
import gevorgyan.pkmn.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StudentDao {
    @Autowired
    private final StudentRepository studentRepository;

    public List<StudentEntity> findAll() {
        return studentRepository.findAll();
    }

    public StudentEntity save(StudentEntity student) {
        return studentRepository.save(student);
    }

    public void deleteById(UUID id) {
        studentRepository.deleteById(id);
    }

    public List<StudentEntity> findByGroup(String group) {
        return studentRepository.findByGroup(group);
    }

    public Optional<StudentEntity> findByFullName(String firstName, String surName, String familyName) {
        return studentRepository.findByFirstNameAndSurNameAndFamilyName(firstName, surName, familyName);
    }

    public Optional<StudentEntity> findById(UUID id) {
        return studentRepository.findById(id);
    }
}