package cn.edu.tju.controller;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.tju.service.StudentService;

@RestController
public class StudentController {
	
	@Resource
	private StudentService studentServiceImpl ; 
	
	@RequestMapping("steal")
	public void stealClass(String lessonName) {
		try {
			studentServiceImpl.selectClass(lessonName);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
