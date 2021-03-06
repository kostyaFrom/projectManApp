package com.konstantinbulygin.pmwebapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konstantinbulygin.pmwebapp.dto.ChartData;
import com.konstantinbulygin.pmwebapp.dto.EmployeeProject;
import com.konstantinbulygin.pmwebapp.entities.Project;
import com.konstantinbulygin.pmwebapp.services.EmployeeService;
import com.konstantinbulygin.pmwebapp.services.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/")
public class HomeController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public HomeController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

//    @GetMapping
//    public String showLogin(){
//        return "security/login";
//    }

    @GetMapping
    public String displayHomeView(Model model) throws JsonProcessingException {


        //querying the database for projects
        List<Project> projects = projectService.getAll();
        //sending the data of projects to home view
        model.addAttribute("projectsList", projects);

        List<ChartData> projectData = projectService.getProjectStatus();

        //covert projectData into a json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(projectData);
        model.addAttribute("projectStatusCnt", json);


        //querying the database for employees
        List<EmployeeProject> employeeProjectsCount = employeeService.getEmployeeProjects();
        //sending the data of employees to home view
        model.addAttribute("employeeProjectsCount", employeeProjectsCount);

        return "main/home";
    }
}
