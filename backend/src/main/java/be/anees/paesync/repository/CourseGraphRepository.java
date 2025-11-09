package be.anees.paesync.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import be.anees.paesync.model.CourseNode;

public interface CourseGraphRepository extends Neo4jRepository<CourseNode, String> {
    // Find all courses that are prerequisites for the course whose acronym is $acronym
    @Query("""
        MATCH (p:Course)-[:PREREQUISITE_OF]->(c:Course {acronym: $acronym})
        RETURN p
    """)
    List<CourseNode> findPrerequisites(String acronym);

    // Create a prerequisite relation
    @Query("""
        MATCH (a:Course {acronym: $from}), (b:Course {acronym: $to})
        MERGE (a)-[:PREREQUISITE_OF]->(b)
    """)
    void addPrerequisite(String from, String to);

    // Find the course node with acronym $from and the one with acronym $to, then remove the PREREQUISITE_OF relationship between them.
    @Query("""
        MATCH (a:Course {acronym: $from})-[r:PREREQUISITE_OF]->(b:Course {acronym: $to})
        DELETE r
    """)
    void removePrerequisite(String from, String to);
}