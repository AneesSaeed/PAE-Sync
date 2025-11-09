package be.anees.paesync.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "courses")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseSearch {
    @Id
    private String acronym;

    @Field(type = FieldType.Text)
    private String title;    
}
