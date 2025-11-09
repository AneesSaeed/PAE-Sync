package be.anees.paesync.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import be.anees.paesync.model.CourseSearch;

public interface CourseSearchRepository extends ElasticsearchRepository<CourseSearch, String>{
    // * before and after ?0 means “match anything before or after the user’s term.”
    // The match part analyzes text — it splits "Bases de données 1" into words like ["bases", "données"], so typing "persistance des données" matches by the shared token "données".
    @Query("""
    {
    "bool": {
        "should": [
            { 
                "wildcard": { 
                    "acronym": { 
                    "value": "*?0*", 
                    "case_insensitive": true 
                    } 
                } 
            },
            { 
                "match": { 
                    "title": { 
                        "query": "?0", 
                        "fuzziness": "AUTO" 
                        } 
                    } 
            }
        ]
    }
    }
    """)
    List<CourseSearch> fuzzySearch(String term);
}
