package be.anees.paesync.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.anees.paesync.model.Course;
import be.anees.paesync.model.CourseNode;
import be.anees.paesync.model.CourseSearch;
import be.anees.paesync.repository.CourseGraphRepository;
import be.anees.paesync.repository.CourseMongoRepository;
import be.anees.paesync.repository.CourseSearchRepository;

@Service
public class CourseService {
    private static final Logger log = LoggerFactory.getLogger(CourseService.class);


    @Autowired
    private CourseMongoRepository mongoCourseRepository;

    @Autowired
    private CourseGraphRepository neo4jCourseRepository;

    @Autowired
    private CourseSearchRepository courseSearchRepository;


    public Course save(Course course) {
        Course savedCourse = saveToMongo(course);
        
        try {
            saveToNeo4j(savedCourse);
            saveToElasticsearch(savedCourse);
            return savedCourse;
        } catch (Exception e) {
            handleFailure(savedCourse, e);
            throw new RuntimeException("Transaction failed: rollback executed", e);
        }
    }


    public void delete(String acronym) {
        Course course = mongoCourseRepository.findByAcronym(acronym)
                .orElseThrow(() -> new RuntimeException("Cours introuvable : " + acronym));

        try {
            deleteFromMongo(acronym);
            deleteFromNeo4j(acronym);
            deleteFromElasticsearch(acronym);

            log.info("Course {} deleted successfully from MongoDB, Neo4j, and Elasticsearch", acronym);
        } catch (Exception e) {
            log.error("Deletion failed for {}. Attempting rollback...", acronym, e);
            handleDeleteFailure(course, e);
            throw new RuntimeException("Transaction failed: rollback executed", e);
        }
    }

    public List<Course> getAll() {
        return mongoCourseRepository.findAll();
    }

    public Course getByAcronym(String acronyme) {
    return mongoCourseRepository.findByAcronym(acronyme)
            .orElseThrow(() -> new RuntimeException("Cours non trouvé : " + acronyme));
    }


    // only for initial synchronization of neo4j and elasticsearch with mongodb
    public void syncMongoToNeo4jAndElasticsearch() {
        var allCourses = mongoCourseRepository.findAll();

        int neo4jCreated = 0;
        int elasticCreated = 0;
        int totalCourses = allCourses.size();

        for (Course course : allCourses) {
            // --- Sync Neo4j ---
            if (!neo4jCourseRepository.existsById(course.getAcronym())) {
                neo4jCourseRepository.save(new CourseNode(course.getAcronym()));
                neo4jCreated++;
            }

            // --- Sync Elasticsearch ---
            if (!courseSearchRepository.existsById(course.getAcronym())) {
                courseSearchRepository.save(
                    new CourseSearch(course.getAcronym(), course.getTitle())
                );
                elasticCreated++;
            }
        }

        log.info(
            "Full sync complete for {} courses — created {} Neo4j nodes and {} Elasticsearch documents.",
            totalCourses, neo4jCreated, elasticCreated
        );
    }


    // -------------------------------
    // -------- Helper methods -------
    // -------------------------------

    // ---- Deletion helpers ----

    private Course saveToMongo(Course course) {
        try {
            Course saved = mongoCourseRepository.save(course);
            log.info("Saved course {} in MongoDB", saved.getAcronym());
            return saved;
        } catch (Exception e) {
            log.error("MongoDB save failed for course {}", course.getAcronym(), e);
            throw new RuntimeException("MongoDB save failed", e);
        }
    }

    private void saveToNeo4j(Course course) {
        neo4jCourseRepository.save(new CourseNode(course.getAcronym()));
        log.info("Saved course {} in Neo4j", course.getAcronym());
    }

    private void saveToElasticsearch(Course course) {
        courseSearchRepository.save(new CourseSearch(course.getAcronym(), course.getTitle()));
        log.info("Indexed course {} in Elasticsearch", course.getAcronym());
    }

    private void handleFailure(Course course, Exception originalException) {
        log.error("Sync failed for {} — rolling back all changes", course.getAcronym(), originalException);

        rollbackNeo4j(course);
        rollbackMongo(course);
    }

    private void rollbackNeo4j(Course course) {
        try {
            neo4jCourseRepository.deleteById(course.getAcronym());
            log.warn("Rolled back Neo4j node for {}", course.getAcronym());
        } catch (Exception e) {
            log.error("Failed to roll back Neo4j node for {}", course.getAcronym(), e);
        }
    }

    private void rollbackMongo(Course course) {
        try {
            mongoCourseRepository.deleteById(course.getAcronym());
            log.warn("Rolled back MongoDB document for {}", course.getAcronym());
        } catch (Exception e) {
            log.error("Failed to roll back MongoDB document for {}", course.getAcronym(), e);
        }
    }


    // ---- Deletion helpers ----

    private void deleteFromMongo(String acronym) {
        try {
            mongoCourseRepository.deleteById(acronym);
            log.info("Deleted {} from MongoDB", acronym);
        } catch (Exception e) {
            log.error("MongoDB deletion failed for {}", acronym, e);
            throw new RuntimeException("MongoDB deletion failed", e);
        }
    }

    private void deleteFromNeo4j(String acronym) {
        try {
            neo4jCourseRepository.deleteById(acronym);
            log.info("Deleted {} from Neo4j", acronym);
        } catch (Exception e) {
            log.error("Neo4j deletion failed for {}", acronym, e);
            throw new RuntimeException("Neo4j deletion failed", e);
        }
    }

    private void deleteFromElasticsearch(String acronym) {
        try {
            courseSearchRepository.deleteById(acronym);
            log.info("Deleted {} from Elasticsearch", acronym);
        } catch (Exception e) {
            log.error("Elasticsearch deletion failed for {}", acronym, e);
            throw new RuntimeException("Elasticsearch deletion failed", e);
        }
    }


    private void handleDeleteFailure(Course course, Exception originalException) {
        String acronym = course.getAcronym();
        log.warn("Rolling back deletion for {}", acronym, originalException);

        // Mongo rollback
        if (!mongoCourseRepository.existsById(acronym)) {
            try {
                mongoCourseRepository.save(course);
                log.warn("Restored MongoDB document for {}", acronym);
            } catch (Exception e) {
                log.error("Failed to restore MongoDB document for {}", acronym, e);
            }
        }

        // Neo4j rollback
        if (!neo4jCourseRepository.existsById(acronym)) {
            try {
                neo4jCourseRepository.save(new CourseNode(acronym));
                log.warn("Restored Neo4j node for {}", acronym);
            } catch (Exception e) {
                log.error("Failed to restore Neo4j node for {}", acronym, e);
            }
        }

        // Elasticsearch rollback
        if (!courseSearchRepository.existsById(acronym)) {
            try {
                courseSearchRepository.save(new CourseSearch(acronym, course.getTitle()));
                log.warn("Restored Elasticsearch document for {}", acronym);
            } catch (Exception e) {
                log.error("Failed to restore Elasticsearch document for {}", acronym, e);
            }
        }
    }

}
