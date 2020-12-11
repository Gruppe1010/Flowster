package gruppe10.flowster.controllers;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.users.User;
import gruppe10.flowster.services.ProjectService;
import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

@Controller
@RequestMapping("/{orgDbName}")
public class ProjectController
{
    UserService userService = new UserService();
    ProjectService projectService = new ProjectService();

    @GetMapping("/projects")
    public String projects(@PathVariable String orgDbName, Model orgDbNameModel,
                           Model loggedInUserModel, Model joinedProjectsListModel)
    {
        //TODO projectService.updateJoinedProjectsList();
    
        ArrayList<Project> joinedProjectsList = new ArrayList<>();

        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);

        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectsListModel.addAttribute("joinedProjectsList", joinedProjectsList);

        return "project/projects"; // html
    }

    @GetMapping("/createProject")
    public String createProject(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel,
                                Model joinedProjectsListModel)
    {
        //TODO projectService.updateJoinedProjectsList();
        ArrayList<Project> joinedProjectsList = new ArrayList<>();
    
        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
    
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectsListModel.addAttribute("joinedProjectsList", joinedProjectsList);
        
        return "project/create-project"; // html
    }
    
    
    @PostMapping("/createProject")
    public String postCreateProject(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel,
                                 Model joinedProjectsList, WebRequest dataFromCreateProjectForm)
    {
        // TODO FØRST
        // opret tabel: projects_users - id_projects_user INT NOT NULL AUTO_INCREMENT primary, f_id_project INT NOT
        // NULL UQ, f_id_user NN
        // opret projekt og gem i db
        int projectId = 0; // HENT SENEST TILFØJEDE projekts id fra db
        
        return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
    }
    
    @GetMapping("/editProject/{projectId}")
    public String editProject(@PathVariable String orgDbName, @PathVariable int projectId,
                              Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectsListModel)
    {
    
        ArrayList<Project> joinedProjectsList = new ArrayList<>();
    
        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
    
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectsListModel.addAttribute("joinedProjectsList", joinedProjectsList);
        
        
        
        
        
        
        
        // på denne html:
        // tilføj delprojekt-KNAP= th:href="”${’/’ + orgDbName + ’/editProject/’ + projectModel.getId() +
        // ’/addSubproject/’ + nextSubprojectId}”
    
        // HER er der knapper som linker videre til GetMappings nedenunder
        // + tilføj opgave-KNAP – th:href=”${’/’ + orgDbName + ’/editProject/’ + projectModel.getId() + ’/subproject/’
        // + subproject.getId() + ’/addTask/’ + nextTaskId}”
        return "project/edit-project"; // html
    }
    

    // tilføj delprojekt-KNAP
    @GetMapping("/editProject/{projectId}/createSubproject/{nextSubprojectId}")
    public String addSubproject(@PathVariable String orgDbName, @PathVariable int projectId,
                                @PathVariable int nextSubprojectId,
                                Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectsListModel,
                                Model projectIdModel, Model nextSubprojectIdModel)
    {
        ArrayList<Project> joinedProjectsList = new ArrayList<>();
    
        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
    
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectsListModel.addAttribute("joinedProjectsList", joinedProjectsList);
        
        
        
        
    
        // modeller til th:action i form i html
        projectIdModel.addAttribute("projectId", projectId);
        nextSubprojectIdModel.addAttribute("nextSubprojectId", nextSubprojectId);
        
        // tilføj FORM med postMapping:
        // th:action="${'/editProject/' + projectId + /createSubproject/' + nextSubprojectId}" method="post"
        // 'tilføj delprojekt'-KNAP SKAL IKKE VÆRE DER
        return "project/create-subproject"; // html
    }
    
    //
    @PostMapping("/editProject/{projectId}/createSubproject/{subprojectId}")
    public String postCreateSubproject(@PathVariable String orgDbName, @PathVariable int projectId,
                                       @PathVariable int subprojectId,
                                       Model orgDbNameModel, Model loggedInUserModel,
                                       Model joinedProjectsList, WebRequest dataFromCreateSubprojectForm)
    {
        // opret subproject og gem i db
        int subProjectId = 0; // HENT SENEST TILFØJEDE projekts id fra db
        
        return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
    }
    
    
    // tilføj task-KNAP
    @GetMapping("/editProject/{projectId}/subproject/{subprojectId}/createTask/{nextTaskId}")
    public String addTask(@PathVariable String orgDbName, @PathVariable int projectId,
                          @PathVariable int subprojectId, @PathVariable int nextTaskId,
                          Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectsListModel,
                          Model projectIdModel, Model subprojectIdModel, Model nextTaskIdModel)
    {
        ArrayList<Project> joinedProjectsList = new ArrayList<>();
    
        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
    
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectsListModel.addAttribute("joinedProjectsList", joinedProjectsList);
        
        
        
        
        
    
        // modeller til th:action i form i html
        projectIdModel.addAttribute("projectId", projectId);
        subprojectIdModel.addAttribute("subprojectId", subprojectId);
        nextTaskIdModel.addAttribute("nextTaskId", nextTaskId);
        
        
        // tilføj FORM med postMapping:
        // th:action="${'/editProject/' + projectId + '/subproject/' + subprojectId + '/createTask/' + nextTaskId}"
        // method="post"
        // tilføj opgave-KNAP SKAL IKKE VÆRE DER
        return "project/create-task"; // html
    }
    
    @PostMapping("/editProject/{projectId}/subproject/{subprojectId}/createTask/{taskId}")
    public String postCreateTask(@PathVariable String orgDbName, @PathVariable int projectId,
                                 @PathVariable int subprojectId, @PathVariable int taskId,
                                 Model orgDbNameModel, Model loggedInUserModel,
                                 Model joinedProjectsList, WebRequest dataFromCreateTaskForm)
    {
        
        
        return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
    }
    
    // tilføj subtask-KNAP
    @GetMapping("/editProject/{projectId}/subproject/{subprojectId}/task/{taskId}/createSubtask/{nextSubtaskId}")
    public String createSubtask(@PathVariable String orgDbName, @PathVariable int projectId,
                             @PathVariable int subprojectId, @PathVariable int taskId,
                             @PathVariable int nextSubtaskId, Model orgDbNameModel,
                             Model loggedInUserModel, Model joinedProjectsListModel,
                             Model projectIdModel, Model subprojectIdModel, Model taskIdModel, Model nextSubtaskIdModel)
    {
    
        ArrayList<Project> joinedProjectsList = new ArrayList<>();
    
        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
    
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectsListModel.addAttribute("joinedProjectsList", joinedProjectsList);
        
        
        
    
        // modeller til th:action i form i html
        projectIdModel.addAttribute("projectId", projectId);
        subprojectIdModel.addAttribute("subprojectId", subprojectId);
        taskIdModel.addAttribute("taskId", taskId);
        nextSubtaskIdModel.addAttribute("nextSubtaskId", nextSubtaskId);
        
        
        
        // tilføj FORM med postMapping:
        // th:action="${'/editProject/' + projectId + '/subproject/' + subprojectId + '/task/' + taskId +
        // '/createSubtask/ + nextSubtaskId}"
        // method="post"
        // tilføj underopgave-KNAP SKAL IKKE VÆRE DER
        return "project/create-subtask"; // html
    }
    
    
    
    @PostMapping("/editProject/{projectId}/subproject/{subprojectId}/task/{taskId}/createSubtask/{subtaskId}")
    public String postCreateSubtask(@PathVariable String orgDbName, @PathVariable int projectId,
                                    @PathVariable int subprojectId, @PathVariable int taskId,
                                    @PathVariable int subTaskId,
                                    Model orgDbNameModel, Model loggedInUserModel,
                                    Model joinedProjectsList, WebRequest dataFromCreateSubtaskForm)
    {
      
        
        return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
    }
   
    




}
