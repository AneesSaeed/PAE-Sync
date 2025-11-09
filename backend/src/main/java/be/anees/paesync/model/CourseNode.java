package be.anees.paesync.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Node("Course") // this tells spring data neo4j that this Java class represents a node in the graph with the label Course
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseNode {
    @Id
    private String acronym;
    
    @Relationship(type = "PREREQUISITE_OF") // This course has outgoing relationships of type PREREQUISITE_OF that connect it to other CourseNodes, stored in a set called unlocks
    //why set ? because a node can have multiple relationships of the same type to different nodes. 
    private Set<CourseNode> unlocks = new HashSet<>();
    
    public CourseNode(String acronym) {
        this.acronym = acronym;
    }    
}
