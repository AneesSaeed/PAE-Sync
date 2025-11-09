package be.anees.paesync.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.anees.paesync.model.Course;
import be.anees.paesync.model.CourseSearch;
import be.anees.paesync.service.CourseSearchService;
import be.anees.paesync.service.CourseService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CourseSearchService courseSearchService;

    // ------------------------------------
    // Basic CRUD
    // ------------------------------------

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAll();
    }

    @GetMapping("/{acronym}")
    public Course getCourse(@PathVariable String acronym) {
        return courseService.getByAcronym(acronym);
    }

    @PostMapping
    public Course addCourse(@Valid @RequestBody Course course) {
        return courseService.save(course);
    }

    @DeleteMapping("/{acronym}")
    public void deleteCourse(@PathVariable String acronym) {
        courseService.delete(acronym);
    }

    // ------------------------------------
    // Fuzzy Search
    // ------------------------------------
    @GetMapping("/search")
    public List<CourseSearch> searchCourses(@RequestParam("q") String query) {
        return courseSearchService.fuzzySearch(query);
    }
}
