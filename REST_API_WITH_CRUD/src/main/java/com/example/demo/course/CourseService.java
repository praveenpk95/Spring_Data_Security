package com.example.demo.course;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.topic.Topic;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;
	
	public List<Course> getAllCourses(String topicId) {
		List<Course> Courses = new ArrayList<>();
		
		courseRepository.findByTopicId(topicId)
						.forEach(Courses::add);
		
		return Courses;
	}
	
	public Course getCourse(String id) {
		return courseRepository.findById(id).get();
	}

	public void addCourse(Course Course) {
		courseRepository.save(Course);
	}

	public void updateCourse(Course Course) {
		courseRepository.save(Course);
	}

	public void deleteCourse(String id) {
		courseRepository.deleteById(id);
	}

	public void setTopic(Topic topic) {		
	}
}
