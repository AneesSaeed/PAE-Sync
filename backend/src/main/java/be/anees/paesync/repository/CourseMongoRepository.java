package be.anees.paesync.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import be.anees.paesync.model.Course;

public interface CourseMongoRepository extends MongoRepository<Course, String> {
    Optional<Course> findByAcronym(String acronym); 
} 
