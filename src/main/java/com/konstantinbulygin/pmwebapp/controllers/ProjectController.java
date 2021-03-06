package com.konstantinbulygin.pmwebapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konstantinbulygin.pmwebapp.dto.TimeChartData;
import com.konstantinbulygin.pmwebapp.entities.Employee;
import com.konstantinbulygin.pmwebapp.entities.Project;
import com.konstantinbulygin.pmwebapp.services.EmployeeService;
import com.konstantinbulygin.pmwebapp.services.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;


    public ProjectController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @GetMapping("/new")
    public String displayProjectForm(Model model) {
        List<Employee> allEmployees = employeeService.getAll();

        model.addAttribute("project", new Project());
        model.addAttribute("allEmployees", allEmployees);
        return "projects/new-project";
    }

    //saving project to DB
    @PostMapping("/save")
    public String saveProject(@Valid Project project, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //return to view if project has errors in fields
        if (bindingResult.hasErrors()) {
            List<Employee> allEmployees = employeeService.getAll();
            model.addAttribute("allEmployees", allEmployees);
            return "projects/new-project";
        }

        if (project.getStartDate().getTime() > project.getEndDate().getTime()) {
            List<Employee> allEmployees = employeeService.getAll();
            model.addAttribute("allEmployees", allEmployees);
            model.addAttribute("dateValidation", "Please choose a proper range date");
            return "projects/new-project";
            //return "redirect:/projects/new";
        }

        if (project.getEmployees().size() == 0) {
            model.addAttribute("employeeValidation", "Please choose one or more employees");
            List<Employee> allEmployees = employeeService.getAll();
            model.addAttribute("allEmployees", allEmployees);
            return "projects/new-project";
        }

        redirectAttributes.addFlashAttribute("message", "Project added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        projectService.save(project);

        return "redirect:/projects/new";
    }

    @GetMapping
    public String displayProjects(Model model) {

        //querying the database for projects
        List<Project> projects = projectService.getAll();
        //sending the data of projects to home view
        model.addAttribute("projectsList", projects);

        return "projects/list-projects";
    }

    @GetMapping("/edit/{id}")
    public String displayEditProject(@PathVariable long id, Model model) throws ParseException {

        List<Employee> allEmployees = employeeService.getAll();
        Project project = projectService.getProjectById(id);

        model.addAttribute("allEmployees", allEmployees);
        model.addAttribute("project", project);
        return "projects/new-project";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable long id) {
        projectService.delete(id);
        return "redirect:/projects";
    }


    @GetMapping("/timelines")
    public String displayProjectTimelines(Model model) throws JsonProcessingException {

        //querying the database for projects
        List<TimeChartData> timeLineData = projectService.getTimeData();

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonTimeLine = objectMapper.writeValueAsString(timeLineData);

        //sending the data of projects to home view
        model.addAttribute("projectTimeList", jsonTimeLine);

        return "projects/project-timelines";
    }

}
