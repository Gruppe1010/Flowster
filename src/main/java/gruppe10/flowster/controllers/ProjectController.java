package gruppe10.flowster.controllers;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.services.ProjectService;
import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.project.CreateProjectViewModel;
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
    ProjectService projectService = new ProjectService();

    @GetMapping("/projects")
    public String projects(@PathVariable String orgDbName, Model orgDbNameModel,
                           Model loggedInUserModel, Model joinedProjectListModel)
    {
        // projektliste til sidebar
        ArrayList<Project> joinedProjectList = projectService.updateJoinedProjectList(orgDbName);
        
        orgDbNameModel.addAttribute("orgDbName", orgDbName);

        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", joinedProjectList);

        return "project/projects"; // html
    }

    @GetMapping("/createProject")
    public String createProject(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel,
                                Model joinedProjectListModel)
    {
        // projektliste til sidebar
        ArrayList<Project> joinedProjectList = projectService.updateJoinedProjectList(orgDbName);
        
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
    
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", joinedProjectList);
        
        return "project/create-project"; // html
    }
    
    // FÆRDIG
    @PostMapping("/createProject")
    public String postCreateProject(@PathVariable String orgDbName, WebRequest dataFromCreateProjectForm)
    {
        // opret CreateProjectViewModel(dataFromCreateProjectForm) ud fra webRequest
        CreateProjectViewModel createProjectViewModel =
                projectService.createProjectViewModelFromForm(dataFromCreateProjectForm);
    
        
        // tjek om projekttitel optaget
        boolean projectTitleIsAvailable = projectService.isProjectTitleAvailable(orgDbName, createProjectViewModel);
    
        // hvis projectTitle ikke findes allerede
        if(projectTitleIsAvailable)
        {
            // tilføj nyt projekt til db
            projectService.insertNewProjectIntoDb(orgDbName, createProjectViewModel);
            
            // vi henter id på nyoprettet projekt
            int projectId = projectService.retrieveProjectIdFromProjectTitle(orgDbName, createProjectViewModel.getTitle());
            
            // knyt bruger til nyoprettet projekt
            projectService.attachCreatorToCreatedProject(orgDbName, projectId, UserService.loggedInUser.getId());
        
            return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
        }
        
        return String.format("redirect:/%s/createProject", orgDbName);
    }
    
    @GetMapping("/editProject/{projectId}")
    public String editProject(@PathVariable String orgDbName, @PathVariable int projectId,
                              Model orgDbNameModel, Model loggedInUserModel,
                              Model joinedProjectListModel, Model projectModel,
                              Model projectIdModel, Model nextSubprojectIdModel,
                              Model nextTaskIdModel, Model nextSubtaskIdModel)
    {
        // projektliste til sidebar
        ArrayList<Project> joinedProjectList = projectService.updateJoinedProjectList(orgDbName);
        
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", joinedProjectList);
        
        
        // modeller til main content
        /*
        projectModel.addAttribute("projectModel", new Project(1, "Eksamensprojekt-projekt", null, 30,
                new ArrayList<Subproject>(Arrays.asList(
                        new Subproject(1, "Virksomhed-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(1, "Risikoanalyse-opgave",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(1, "Risikotabel-underopgave"),
                                                        new Subtask(2, "Beskrivelse af risikomomenter-underopgave"))))))),
                        new Subproject(2, "Systemudvikling-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(2, "Use case model-opgave",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(3, "Use case diagram-underopgave"),
                                                        new Subtask(4, "SSD'er-underopgave")))),
                                        new Task(3, "FURPS",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(5, "Funktional-underopgave"),
                                                        new Subtask(6, "Non-funktional-underopgave"))))))),
                        new Subproject(3, "Programmering-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(4, "Kode-opgave")))))),
                null)); // TODO ret til hent project fra db
                
         */
        projectModel.addAttribute("project", projectService.retrieveProject(orgDbName, projectId));
        projectIdModel.addAttribute("projectId", projectId);
        nextSubprojectIdModel.addAttribute("nextSubprojectId", projectService.findNextIdFromTable(orgDbName, "subprojects"));
        nextTaskIdModel.addAttribute("nextTaskId", projectService.findNextIdFromTable(orgDbName, "tasks"));
        nextSubtaskIdModel.addAttribute("nextSubtaskId", projectService.findNextIdFromTable(orgDbName, "subtasks"));
        
        
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
    public String createSubproject(@PathVariable String orgDbName, @PathVariable int projectId,
                                @PathVariable int nextSubprojectId,
                                Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectListModel,
                                Model projectModel, Model projectIdModel, Model nextSubprojectIdModel,
                                Model nextTaskIdModel, Model nextSubtaskIdModel)
    {
        // projektliste til sidebar
        ArrayList<Project> joinedProjectList = projectService.updateJoinedProjectList(orgDbName);
        
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", joinedProjectList);
    
    
        // modeller til main content
        /*
        projectModel.addAttribute("projectModel", new Project(1, "Eksamensprojekt-projekt", null, 30,
                new ArrayList<Subproject>(Arrays.asList(
                        new Subproject(1, "Virksomhed-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(1, "Risikoanalyse-opgave",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(1, "Risikotabel-underopgave"),
                                                        new Subtask(2, "Beskrivelse af risikomomenter-underopgave"))))))),
                        new Subproject(2, "Systemudvikling-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(2, "Use case model-opgave",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(3, "Use case diagram-underopgave"),
                                                        new Subtask(4, "SSD'er-underopgave")))),
                                        new Task(3, "FURPS",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(5, "Funktional-underopgave"),
                                                        new Subtask(6, "Non-funktional-underopgave"))))))),
                        new Subproject(3, "Programmering-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(4, "Kode-opgave")))))),
                null)); // TODO ret til hent project fra db
                
         */
        projectModel.addAttribute("project", projectService.retrieveProject(orgDbName, projectId));
        projectIdModel.addAttribute("projectId", projectId);
        nextSubprojectIdModel.addAttribute("nextSubprojectId", projectService.findNextIdFromTable(orgDbName, "subprojects"));
        nextTaskIdModel.addAttribute("nextTaskId", projectService.findNextIdFromTable(orgDbName, "tasks"));
       
       
        // modeller til th:action i form i html
        nextSubtaskIdModel.addAttribute("nextSubtaskId", projectService.findNextIdFromTable(orgDbName, "subtasks"));
      
        
        
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
                                       Model joinedProjectList, WebRequest dataFromCreateSubprojectForm)
    {
        // opret subproject og gem i db
        int subProjectId = 0; // HENT SENEST TILFØJEDE projekts id fra db
        
        return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
    }
    
    
    // tilføj task-KNAP
    @GetMapping("/editProject/{projectId}/subproject/{subprojectId}/createTask/{nextTaskId}")
    public String createTask(@PathVariable String orgDbName, @PathVariable int projectId,
                          @PathVariable int subprojectId, @PathVariable int nextTaskId,
                          Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectListModel,
                          Model projectModel, Model nextSubprojectIdModel,  Model nextTaskIdModel,
                          Model nextSubtaskIdModel, Model projectIdModel, Model subprojectIdModel)
    {
        // projektliste til sidebar
        ArrayList<Project> joinedProjectList = projectService.updateJoinedProjectList(orgDbName);
        
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", joinedProjectList);
    
    
        // modeller til main content
        /*
        projectModel.addAttribute("projectModel", new Project(1, "Eksamensprojekt-projekt", null, 30,
                new ArrayList<Subproject>(Arrays.asList(
                        new Subproject(1, "Virksomhed-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(1, "Risikoanalyse-opgave",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(1, "Risikotabel-underopgave"),
                                                        new Subtask(2, "Beskrivelse af risikomomenter-underopgave"))))))),
                        new Subproject(2, "Systemudvikling-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(2, "Use case model-opgave",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(3, "Use case diagram-underopgave"),
                                                        new Subtask(4, "SSD'er-underopgave")))),
                                        new Task(3, "FURPS",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(5, "Funktional-underopgave"),
                                                        new Subtask(6, "Non-funktional-underopgave"))))))),
                        new Subproject(3, "Programmering-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(4, "Kode-opgave")))))),
                null)); // TODO ret til hent project fra db
               
         */
        projectModel.addAttribute("project", projectService.retrieveProject(orgDbName, projectId));
        projectIdModel.addAttribute("projectId", projectId);
        subprojectIdModel.addAttribute("subprojectId", subprojectId);
        nextSubprojectIdModel.addAttribute("nextSubprojectId", projectService.findNextIdFromTable(orgDbName, "subprojects"));
        nextSubtaskIdModel.addAttribute("nextSubtaskId", projectService.findNextIdFromTable(orgDbName, "subtasks"));
      
        
        // modeller til th:action i form i html
        nextTaskIdModel.addAttribute("nextTaskId", projectService.findNextIdFromTable(orgDbName, "tasks"));
      
        
        
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
                                 Model joinedProjectList, WebRequest dataFromCreateTaskForm)
    {
        
        
        return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
    }
    
    // tilføj subtask-KNAP
    @GetMapping("/editProject/{projectId}/subproject/{subprojectId}/task/{taskId}/createSubtask/{nextSubtaskId}")
    public String createSubtask(@PathVariable String orgDbName, @PathVariable int projectId,
                             @PathVariable int subprojectId, @PathVariable int taskId,
                             @PathVariable int nextSubtaskId, Model orgDbNameModel,
                             Model loggedInUserModel, Model joinedProjectListModel, Model projectModel,
                             Model projectIdModel, Model subprojectIdModel, Model taskIdModel,
                                Model nextSubprojectIdModel, Model nextTaskIdModel, Model nextSubtaskIdModel)
    {
        // projektliste til sidebar
        ArrayList<Project> joinedProjectList = projectService.updateJoinedProjectList(orgDbName);
    
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", joinedProjectList);
    
    
        // modeller til main content
        /*
        projectModel.addAttribute("projectModel", new Project(1, "Eksamensprojekt-projekt", null, 30,
                new ArrayList<Subproject>(Arrays.asList(
                        new Subproject(1, "Virksomhed-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(1, "Risikoanalyse-opgave",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(1, "Risikotabel-underopgave"),
                                                        new Subtask(2, "Beskrivelse af risikomomenter-underopgave"))))))),
                        new Subproject(2, "Systemudvikling-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(2, "Use case model-opgave",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(3, "Use case diagram-underopgave"),
                                                        new Subtask(4, "SSD'er-underopgave")))),
                                        new Task(3, "FURPS",
                                                new ArrayList<Subtask>(Arrays.asList(
                                                        new Subtask(5, "Funktional-underopgave"),
                                                        new Subtask(6, "Non-funktional-underopgave"))))))),
                        new Subproject(3, "Programmering-delprojekt",
                                new ArrayList<Task>(Arrays.asList(
                                        new Task(4, "Kode-opgave")))))),
                null)); // TODO ret til hent project fra db
                
         */
        projectModel.addAttribute("project", projectService.retrieveProject(orgDbName, projectId));
        projectIdModel.addAttribute("projectId", projectId);
        subprojectIdModel.addAttribute("subprojectId", subprojectId);
        taskIdModel.addAttribute("taskId", taskId);
        nextSubprojectIdModel.addAttribute("nextSubprojectId", projectService.findNextIdFromTable(orgDbName, "subprojects"));
        nextSubtaskIdModel.addAttribute("nextSubtaskId", projectService.findNextIdFromTable(orgDbName, "subtasks"));
    
    
        // modeller til th:action i form i html
        nextTaskIdModel.addAttribute("nextTaskId", projectService.findNextIdFromTable(orgDbName, "tasks"));
    
    
    
    
    
    
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
                                    Model joinedProjectList, WebRequest dataFromCreateSubtaskForm)
    {
      
        
        return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
    }
   
    




}
