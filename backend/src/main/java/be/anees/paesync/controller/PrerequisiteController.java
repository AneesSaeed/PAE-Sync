package be.anees.paesync.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.anees.paesync.model.CourseNode;
import be.anees.paesync.service.PrerequisiteService;

@RestController
@RequestMapping("/api/prerequisites")
public class PrerequisiteController {
    
    @Autowired
    private PrerequisiteService prerequisiteService;    
    

    @PostMapping
    public Map<String, String> addPrerequisite(@RequestParam String from, @RequestParam String to) {
        prerequisiteService.addPrerequisite(from, to);
        return Map.of("message", "Prerequisite added: " + from + " → " + to);
    }
    
    @GetMapping("/{course}")
    public List<CourseNode> getPrerequisites(@PathVariable String course) {
        return prerequisiteService.getPrerequisites(course);
    }

    @DeleteMapping
    public Map<String, String> removePrerequisite(@RequestParam String from, @RequestParam String to) {
        prerequisiteService.removePrerequisite(from, to);
        return Map.of("message", "Prerequisite removed: " + from + " → " + to);
    }

    @GetMapping("/nodes")
    public List<String> getCourseNodes() {
        return prerequisiteService.getCourseNodes();
    }
}
