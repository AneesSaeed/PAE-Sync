package be.anees.paesync.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.anees.paesync.model.CourseSearch;
import be.anees.paesync.repository.CourseSearchRepository;

@Service
public class CourseSearchService {
    
    @Autowired    
    private CourseSearchRepository courseSearchRepository;

    public List<CourseSearch> fuzzySearch(String term) {
        return courseSearchRepository.fuzzySearch(term);
    }
}
