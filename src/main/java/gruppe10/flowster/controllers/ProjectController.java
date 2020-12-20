package gruppe10.flowster.controllers;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.services.ProjectService;
import gruppe10.flowster.services.TeamService;
import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.project.CreateProjectViewModel;
import gruppe10.flowster.viewModels.project.CreateSubViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/{orgDbName}")
public class ProjectController
{
    ProjectService projectService = new ProjectService();
    TeamService teamService = new TeamService();
    
    // ViewModeller som bruges til at holde på foreløbig data
    CreateProjectViewModel createProjectViewModel;
    CreateSubViewModel createSubViewModel;

    String error = null;
    
    @GetMapping("/projects")
    public String projects(@PathVariable String orgDbName, Model orgDbNameModel,
                           Model loggedInUserModel, Model joinedProjectListModel)
    {
        // modeller til bars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        // denne er også til main content
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));

        return "project/projects"; // html
    }

    @GetMapping("/createProject")
    public String createProject(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel,
                                Model joinedProjectListModel, Model createProjectModel, Model errorModel,
                                Model currentDateModel)
    {
        // model til form-input-felter
        createProjectModel.addAttribute("createProjectViewModel", createProjectViewModel);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDateModel.addAttribute("currentDate", dateFormat.format(new Date()));
        
        // model til error-popup
        errorModel.addAttribute("error", error);
        
        // modeller til bars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
        
        return "project/create-project"; // html
    }
    
    @PostMapping("/createProject")
    public String postCreateProject(@PathVariable String orgDbName, WebRequest dataFromCreateProjectForm)
    {
        error = null;
        
        // opret CreateProjectViewModel(dataFromCreateProjectForm) ud fra webRequest
        createProjectViewModel =
                projectService.createProjectViewModelFromForm(dataFromCreateProjectForm);
        
        // tjek om projekttitel optaget
        boolean projectTitleIsAvailable = projectService.isProjectTitleAvailable(orgDbName, createProjectViewModel);
    
        // hvis projectTitle ikke findes allerede
        if(projectTitleIsAvailable)
        {
            // tilføjer nyt projekt til db
            projectService.insertNewProjectIntoDb(orgDbName, createProjectViewModel);
            
            // henter id på nyoprettet projekt
            int projectId = projectService.retrieveProjectIdFromProjectTitle(orgDbName, createProjectViewModel.getTitle());
            
            // knytter bruger til nyoprettet projekt
            projectService.attachCreatorToCreatedProject(orgDbName, projectId, UserService.loggedInUser.getId());
            
            // fordi projektet oprettedes succesfuldt skal createProjectViewModel nu ikke vise indtastede titel mere
            createProjectViewModel = null;
        
            return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
        }
        
        error = String.format("Der findes allerede et projekt med titlen \"%s\" i organisationen. " +
                                      "Vælg venligst en anden titel til dit projekt.",
                createProjectViewModel.getTitle());
        
        return String.format("redirect:/%s/createProject/#error-popup", orgDbName);
    }
    
    @GetMapping("/viewProject/{projectId}")
    public String viewProject(@PathVariable String orgDbName, @PathVariable int projectId,
                              Model orgDbNameModel, Model loggedInUserModel,
                              Model joinedProjectListModel, Model projectModel)
    {
        Project project = projectService.retrieveProject(orgDbName, projectId);
        
        // modeller til main content
        projectModel.addAttribute("project", project);
      
        // modeller til bars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
      
        return "project/view-project"; // html
    }
    
    // html indeholder alle "tilføj"-knapper
    @GetMapping("/editProject/{projectId}")
    public String editProject(@PathVariable String orgDbName, @PathVariable int projectId,
                              Model orgDbNameModel, Model loggedInUserModel,
                              Model joinedProjectListModel, Model projectModel,
                              Model projectIdModel, Model nextSubprojectIdModel,
                              Model nextTaskIdModel, Model nextSubtaskIdModel)
    {
        // modeller til main content
        projectModel.addAttribute("project", projectService.retrieveProject(orgDbName, projectId));
        
        // modeller til hrefs
        projectIdModel.addAttribute("projectId", projectId);
        nextSubprojectIdModel.addAttribute("nextSubprojectId", projectService.findNextIdFromTable(orgDbName, "subprojects"));
        nextTaskIdModel.addAttribute("nextTaskId", projectService.findNextIdFromTable(orgDbName, "tasks"));
        nextSubtaskIdModel.addAttribute("nextSubtaskId", projectService.findNextIdFromTable(orgDbName, "subtasks"));
    
        // modeller til bars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
    
        // her vises alle "tilføj"-knapper
        return "project/edit-project"; // html
    }
    
    // rammes når man trykker på "tilføj delprojekt"-KNAP
    @GetMapping("/editProject/{projectId}/createSubproject/{nextSubprojectId}")
    public String createSubproject(@PathVariable String orgDbName, @PathVariable int projectId,
                                @PathVariable int nextSubprojectId,
                                Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectListModel,
                                Model projectModel, Model projectIdModel, Model nextSubprojectIdModel,
                                Model nextTaskIdModel, Model nextSubtaskIdModel, Model createSubprojectModel, Model errorModel)
    {
        // model til form-input-felt
        createSubprojectModel.addAttribute("createSubprojectViewModel", createSubViewModel);
        
        // model til main content
        projectModel.addAttribute("project", projectService.retrieveProject(orgDbName, projectId));
        projectIdModel.addAttribute("projectId", projectId);
        nextTaskIdModel.addAttribute("nextTaskId", projectService.findNextIdFromTable(orgDbName, "tasks"));
        nextSubtaskIdModel.addAttribute("nextSubtaskId", projectService.findNextIdFromTable(orgDbName, "subtasks"));
       
        // Model til th:action i form i html
        nextSubprojectIdModel.addAttribute("nextSubprojectId", nextSubprojectId);
    
        // model til error-popup
        errorModel.addAttribute("error", error);
    
        // modeller til bars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
    
        // her vises form i stedet for "tilføj delprojekt"-knap
        return "project/create-subproject"; // html
    }
    
    
    // Rammes når man trykke gem - ved delprojekt-form
    @PostMapping("/editProject/{projectId}/createSubproject/{subprojectId}")
    public String postCreateSubproject(@PathVariable String orgDbName, @PathVariable int projectId,
                                       @PathVariable int subprojectId, WebRequest dataFromCreateSubprojectForm)
    {
        error = null;
        
        // opret CreateProjectViewModel(dataFromCreateProjectForm) ud fra webRequest
        createSubViewModel =
                projectService.createSubprojectViewModelFromForm(dataFromCreateSubprojectForm);
    
        // tjek om subprojekttitle er optaget
        boolean subprojectTitleIsAvailable = projectService.isSubprojectTitleAvailable(orgDbName, projectId,
                createSubViewModel.getTitle());
    
        // hvis subprojectTitle ikke allerede findes på projektet
        if(subprojectTitleIsAvailable)
        {
            // tilføj nyt subproject til db OG knyt delprojekt til projektet
            projectService.insertNewSubprojectIntoDb(orgDbName, projectId, subprojectId,
                    createSubViewModel);
    
            // fordi delprojektet oprettedes succesfuldt skal createSubprojectViewModel nu ikke vise indtastede titel
            // mere
            createSubViewModel = null;
            
            // vi ryger tilbage til editProject
            return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
        }
    
        error = String.format("Der findes allerede et delprojekt med titlen \"%s\" i dette projekt. " +
                                      "Vælg venligst en anden titel til dit delprojekt.",
                createSubViewModel.getTitle());
        
        // Subprojektet er IKKE blevet gemt i databasen og guider derfor til samme sted, hvor ugyldig title vises pga
        // . createProjectViewModel
        return String.format("redirect:/%s/editProject/%d/createSubproject/%d/#error-popup", orgDbName, projectId,
                subprojectId);
    }
    
    // rammes når man trykker på "tilføj opgave"-knap
    @GetMapping("/editProject/{projectId}/subproject/{subprojectId}/createTask/{nextTaskId}")
    public String createTask(@PathVariable String orgDbName, @PathVariable int projectId,
                          @PathVariable int subprojectId, @PathVariable int nextTaskId,
                          Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectListModel,
                          Model projectModel, Model nextSubprojectIdModel,  Model nextTaskIdModel,
                          Model nextSubtaskIdModel, Model projectIdModel, Model subprojectIdModel,
                          Model createTaskModel, Model errorModel)
    {
        // model til error-popup
        errorModel.addAttribute("error", error);
        
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
       
        // model til form-input-felt
        createTaskModel.addAttribute("createTaskViewModel", createSubViewModel);
        
        projectModel.addAttribute("project", projectService.retrieveProject(orgDbName, projectId));
        projectIdModel.addAttribute("projectId", projectId);
        subprojectIdModel.addAttribute("subprojectId", subprojectId);
        nextSubprojectIdModel.addAttribute("nextSubprojectId", projectService.findNextIdFromTable(orgDbName, "subprojects"));
        nextSubtaskIdModel.addAttribute("nextSubtaskId", projectService.findNextIdFromTable(orgDbName, "subtasks"));
        
        // model til th:action i form i html
        nextTaskIdModel.addAttribute("nextTaskId", nextTaskId);
        
        // her vises form i stedet for "tilføj opgave"-knap
        return "project/create-task"; // html
    }
    
    // rammes når man trykker på "gem" til tilføj opgave
    @PostMapping("/editProject/{projectId}/subproject/{subprojectId}/createTask/{taskId}")
    public String postCreateTask(@PathVariable String orgDbName, @PathVariable int projectId,
                                 @PathVariable int subprojectId, @PathVariable int taskId,
                                 WebRequest dataFromCreateTaskForm, Model exceedModel)
    {
        error = null;
        
        // opret CreateTaskViewModel(dataFromCreateTaskForm) ud fra webRequest
        createSubViewModel = projectService.createSubprojectViewModelFromForm(dataFromCreateTaskForm);
    
        error = String.format("Der findes allerede en opgave med titlen \"%s\" i dette delprojekt. " +
                                                 "Vælg venligst en anden titel til din opgave.",
            createSubViewModel.getTitle());
        
        // tjek om tasktitel allerede findes i delprojekt
        boolean taskTitleIsAvailable = projectService.isTaskTitleAvailable(orgDbName, subprojectId,
                createSubViewModel.getTitle());
    
        // hvis taskTitle ikke allerede findes på subproject'et
        if(taskTitleIsAvailable)
        {
            error = String.format("De indtastede arbejdstimer for opgaven \"%s\" overstiger delprojektet \"%s\"'s " +
                                          "estimerede arbejdstid med %s timer",
                    createSubViewModel.getTitle(), projectService.retrieveSubprojectNameFromId(orgDbName, subprojectId),
                    projectService.calcHoursDiffBetweenSubprojectAndTasks(orgDbName, subprojectId,
                            createSubViewModel.getManhours()));
            
            
            // tjek om indtastede manhours-værdi er for høj ift. projektets timer
            boolean subprojectHoursExceedTaskHours =
                    projectService.doSubrojectHoursExceedTaskHours(orgDbName, subprojectId,
                            createSubViewModel.getManhours());
                            
            // hvis delprojektets timeantal er højere end sammenlagte opgavers timeantal
            if(subprojectHoursExceedTaskHours)
            {
                 // tilføj ny task til db og knyt task til subproject
                 projectService.insertNewTaskIntoDb(orgDbName, subprojectId, taskId,
                         createSubViewModel);
    
                 // fordi delprojektet oprettedes succesfuldt skal createTaskViewModel nu ikke vise indtastede titel
                 // mere
                 createSubViewModel = null;
    
                 // vi ryger tilbage til editProject
                 return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
            }
        }
    
        // Task'en er IKKE blevet gemt i databasen og guider derfor til samme GetMapping, hvor ugyldig title vises
        // via createTaskViewModel
    
        return String.format("redirect:/%s/editProject/%d/subproject/%d/createTask/%d/#error-popup", orgDbName, projectId,
                subprojectId, taskId);
    }
    
    //rammes når man trykker på "tilføj subtask"-KNAP
    @GetMapping("/editProject/{projectId}/subproject/{subprojectId}/task/{taskId}/createSubtask/{nextSubtaskId}")
    public String createSubtask(@PathVariable String orgDbName, @PathVariable int projectId,
                             @PathVariable int subprojectId, @PathVariable int taskId,
                             @PathVariable int nextSubtaskId, Model orgDbNameModel,
                             Model loggedInUserModel, Model joinedProjectListModel, Model projectModel,
                             Model projectIdModel, Model subprojectIdModel, Model taskIdModel,
                                Model nextSubprojectIdModel, Model nextTaskIdModel, Model nextSubtaskIdModel,
                                Model createSubtaskModel, Model errorModel)
    {
        
        projectModel.addAttribute("project", projectService.retrieveProject(orgDbName, projectId));
        
        // til form-knappers LINK
        nextSubprojectIdModel.addAttribute("nextSubprojectId", projectService.findNextIdFromTable(orgDbName, "subprojects"));
        nextTaskIdModel.addAttribute("nextTaskId", projectService.findNextIdFromTable(orgDbName, "tasks"));
        
        // modeller til th:action i form i html
        projectIdModel.addAttribute("projectId", projectId);
        subprojectIdModel.addAttribute("subprojectId", subprojectId);
        taskIdModel.addAttribute("taskId", taskId);
        nextSubtaskIdModel.addAttribute("nextSubtaskId", nextSubtaskId);
    
        // model til form-input-felt
        createSubtaskModel.addAttribute("createSubtaskViewModel", createSubViewModel);
    
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
    
        // model til error-popup
        errorModel.addAttribute("error", error);
    
    
        // tilføj FORM med postMapping:
        // th:action="${'/editProject/' + projectId + '/subproject/' + subprojectId + '/task/' + taskId +
        // '/createSubtask/ + nextSubtaskId}"
        // method="post"
        // tilføj underopgave-KNAP SKAL IKKE VÆRE DER
        return "project/create-subtask"; // html
    }
    
    
    // rammes når man trykker "Gem" i createTask-form
    @PostMapping("/editProject/{projectId}/subproject/{subprojectId}/task/{taskId}/createSubtask/{subtaskId}")
    public String postCreateSubtask(@PathVariable String orgDbName, @PathVariable int projectId,
                                    @PathVariable int subprojectId, @PathVariable int taskId,
                                    @PathVariable int subtaskId, WebRequest dataFromCreateSubtaskForm)
    {
        error = null;
        
        // opret CreateSubtaskViewModel(dataFromCreateSubtaskForm) ud fra webRequest
        createSubViewModel = projectService.createSubprojectViewModelFromForm(dataFromCreateSubtaskForm);
        
        // tjek om tasktitel er optaget
        boolean subtaskTitleIsAvailable = projectService.isSubtaskTitleAvailable(orgDbName, taskId,
                createSubViewModel.getTitle());
    
        // hvis subprojectTitle ikke allerede findes på projektet
        if(subtaskTitleIsAvailable)
        {
            // tilføj nyt subproject til db og knyt delprojekt til projektet
            projectService.insertNewSubtaskIntoDb(orgDbName, taskId, subtaskId,
                    createSubViewModel);
        
            // fordi subtask oprettedes succesfuldt skal createSubtaskViewModel nu ikke vise indtastede titel
            // mere
            createSubViewModel = null;
        
            // vi ryger tilbage til editProject
            return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
        }
    
        error = String.format("Der findes allerede en underopgave med titlen \"%s\" under denne opgave. " +
                                      "Vælg venligst en anden titel til din underopgave.",
                createSubViewModel.getTitle());
    
        // Subtask er IKKE blevet gemt i db --> guide derfor til samme GetMapping, hvor ugyldig title vises
        // via createSubtaskViewModel
        return String.format("redirect:/%s/editProject/%d/subproject/%d/task/%d/createSubtask/%d/#error-popup",
                orgDbName, projectId,
                subprojectId, taskId, subtaskId);
    }
    
    // rammes når man trykker på "tilføj/fjern teams"-knap
    @GetMapping("/editProject/{projectId}/editTeams")
    public String postEditTeams(@PathVariable String orgDbName, @PathVariable int projectId,
                                Model orgDbNameModel, Model loggedInUserModel,
                                Model joinedProjectListModel, Model projectModel, Model projectIdModel,
                                Model joinedTeamListModel, Model errorModel)
    {
        // til main content
        projectIdModel.addAttribute("projectId", projectId);
        joinedTeamListModel.addAttribute("joinedTeamList", teamService.retrieveJoinedTeamList());
        projectModel.addAttribute("project", projectService.retrieveProject(orgDbName, projectId));
        
        // modeller til menu-, sidebar og footer
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
    
        // model til error-popup
        errorModel.addAttribute("error", error);
    
        return "project/edit-teams"; // html
    }
    
    
    // rammes når man på "edit-teams"-siden trykker "tilføj" på et af de viste teams
    @PostMapping("/editProject/{projectId}/addTeam/{teamId}")
    public String postAddTeamToProject(@PathVariable String orgDbName, @PathVariable int projectId,
                                    @PathVariable int teamId)
    {
        projectService.insertRowIntoTeamsProjects(orgDbName, teamId, projectId);
        
        return String.format("redirect:/%s/editProject/%d/editTeams", orgDbName, projectId);
    }
    
    // rammes når man på "edit-teams"-siden trykker "fjern" på et af de viste teams
    @PostMapping("/editProject/{projectId}/removeTeam/{teamId}")
    public String postRemoveTeamFromProject(@PathVariable String orgDbName, @PathVariable int projectId,
                                    @PathVariable int teamId)
    {
        projectService.deleteRowFromTeamsProjects(orgDbName, teamId, projectId);
    
        return String.format("redirect:/%s/editProject/%d/editTeams", orgDbName, projectId);
    }
}
