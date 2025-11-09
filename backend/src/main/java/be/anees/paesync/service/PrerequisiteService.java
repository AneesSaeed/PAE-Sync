package be.anees.paesync.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.anees.paesync.model.CourseNode;
import be.anees.paesync.repository.CourseGraphRepository;
import be.anees.paesync.repository.CourseMongoRepository;

@Service
public class PrerequisiteService {
    
    @Autowired
    private CourseGraphRepository courseGraphRepository;

    @Autowired
    private CourseMongoRepository courseMongoRepository;

    public void addPrerequisite(String from, String to) {
        boolean fromExists = courseMongoRepository.existsById(from);
        boolean toExists = courseMongoRepository.existsById(to);

        if (!fromExists || !toExists) {
            throw new IllegalArgumentException("Both courses must exist in MongoDB before adding a prerequisite.");
        }

        if (!courseGraphRepository.existsById(from) || !courseGraphRepository.existsById(to)) {
            throw new IllegalStateException("Neo4j nodes not found for one or both courses.");
        }

        courseGraphRepository.addPrerequisite(from, to);
    }

    public List<CourseNode> getPrerequisites(String acronym) {
        return courseGraphRepository.findPrerequisites(acronym);
    }

    public void removePrerequisite(String from, String to) {
        courseGraphRepository.removePrerequisite(from, to);
    }

    
    public List<String> getCourseNodes() {
        return courseGraphRepository.findAll()
            .stream()
            .map(CourseNode::getAcronym)
            .sorted()
            .toList();
    }
}
