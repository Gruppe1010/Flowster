package gruppe10.flowster.controllers;

import gruppe10.flowster.services.ProjectService;
import gruppe10.flowster.services.UserService;
import gruppe10.flowster.viewModels.project.CreateProjectViewModel;
import gruppe10.flowster.viewModels.project.CreateSubtaskViewModel;
import gruppe10.flowster.viewModels.project.CreateSubprojectViewModel;
import gruppe10.flowster.viewModels.project.CreateTaskViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/{orgDbName}")
public class ProjectController
{
    ProjectService projectService = new ProjectService();
    
    // ViewModeller som bruges til at holde på foreløbig data
    CreateProjectViewModel createProjectViewModel;
    CreateSubprojectViewModel createSubprojectViewModel;
    CreateTaskViewModel createTaskViewModel;
    CreateSubtaskViewModel createSubtaskViewModel;
    
    
    @GetMapping("/projects")
    public String projects(@PathVariable String orgDbName, Model orgDbNameModel,
                           Model loggedInUserModel, Model joinedProjectListModel)
    {
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));

        return "project/projects"; // html
    }

    @GetMapping("/createProject")
    public String createProject(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel,
                                Model joinedProjectListModel, Model createProjectModel)
    {
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
        
        // model til form-input-felt
        createProjectModel.addAttribute("createProjectViewModel", createProjectViewModel);
        
        
        return "project/create-project"; // html
    }
    
    // FÆRDIG
    @PostMapping("/createProject")
    public String postCreateProject(@PathVariable String orgDbName, WebRequest dataFromCreateProjectForm)
    {
        // opret CreateProjectViewModel(dataFromCreateProjectForm) ud fra webRequest
        createProjectViewModel =
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
            
            // fordi projektet oprettedes succesfuldt skal createProjectViewModel nu ikke vise indtastede titel mere
            createProjectViewModel = null;
        
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
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
        
        
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
    
    // man kommer hertil fra: tilføj delprojekt-KNAP
    @GetMapping("/editProject/{projectId}/createSubproject/{nextSubprojectId}")
    public String createSubproject(@PathVariable String orgDbName, @PathVariable int projectId,
                                @PathVariable int nextSubprojectId,
                                Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectListModel,
                                Model projectModel, Model projectIdModel, Model nextSubprojectIdModel,
                                Model nextTaskIdModel, Model nextSubtaskIdModel, Model createSubprojectModel)
    {
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
    
        // model til form-input-felt
        createSubprojectModel.addAttribute("createSubprojectViewModel", createSubprojectViewModel);
    
    
        // modeller til main content TODO tjek om dette er rigtigt
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
        nextTaskIdModel.addAttribute("nextTaskId", projectService.findNextIdFromTable(orgDbName, "tasks"));
        nextSubtaskIdModel.addAttribute("nextSubtaskId", projectService.findNextIdFromTable(orgDbName, "subtasks"));
       
        // modeller til th:action i form i html
        nextSubprojectIdModel.addAttribute("nextSubprojectId", nextSubprojectId);
        
        // tilføj FORM med postMapping:
        // th:action="${'/editProject/' + projectId + /createSubproject/' + nextSubprojectId}" method="post"
        // 'tilføj delprojekt'-KNAP SKAL IKKE VÆRE DER
        return "project/create-subproject"; // html
    }
    
    
    //
    @PostMapping("/editProject/{projectId}/createSubproject/{subprojectId}")
    public String postCreateSubproject(@PathVariable String orgDbName, @PathVariable int projectId,
                                       @PathVariable int subprojectId, WebRequest dataFromCreateSubprojectForm)
    {
        // opret CreateProjectViewModel(dataFromCreateProjectForm) ud fra webRequest
        createSubprojectViewModel =
                projectService.createSubprojectViewModelFromForm(dataFromCreateSubprojectForm);
    
        // tjek om subprojekttitle er optaget
        boolean subprojectTitleIsAvailable = projectService.isSubprojectTitleAvailable(orgDbName, projectId,
                createSubprojectViewModel.getTitle());
    
        // hvis subprojectTitle ikke allerede findes på projektet
        if(subprojectTitleIsAvailable)
        {
            // tilføj nyt subproject til db OG knyt delprojekt til projektet
            projectService.insertNewSubprojectIntoDb(orgDbName, projectId, subprojectId,
                    createSubprojectViewModel);
    
            // fordi delprojektet oprettedes succesfuldt skal createSubprojectViewModel nu ikke vise indtastede titel
            // mere
            createSubprojectViewModel = null;
            
            // vi ryger tilbage til editProject
            return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
        }
        
        // Subprojektet er IKKE blevet gemt i databasen og guider derfor til samme sted, hvor ugyldig title vises pga
        // . createProjectViewModel
        return String.format("redirect:/%s/editProject/%d/createSubproject/%d", orgDbName, projectId, subprojectId);
    }
    
    
    // tilføj task-KNAP
    @GetMapping("/editProject/{projectId}/subproject/{subprojectId}/createTask/{nextTaskId}")
    public String createTask(@PathVariable String orgDbName, @PathVariable int projectId,
                          @PathVariable int subprojectId, @PathVariable int nextTaskId,
                          Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectListModel,
                          Model projectModel, Model nextSubprojectIdModel,  Model nextTaskIdModel,
                          Model nextSubtaskIdModel, Model projectIdModel, Model subprojectIdModel,
                          Model createTaskModel)
    {
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
    
        // model til form-input-felt
        createTaskModel.addAttribute("createTaskViewModel", createTaskViewModel);
    
    
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
      
        
        // model til th:action i form i html
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
                                 WebRequest dataFromCreateTaskForm)
    {
        // opret CreateTaskViewModel(dataFromCreateTaskForm) ud fra webRequest
        createTaskViewModel = projectService.createTaskViewModelFromForm(dataFromCreateTaskForm);
    
        
        // tjek om tasktitel allerede findes i delprojekt
        boolean taskTitleIsAvailable = projectService.isTaskTitleAvailable(orgDbName, subprojectId,
                createTaskViewModel.getTitle());
    
        // hvis taskTitle ikke allerede findes på subproject'et
        if(taskTitleIsAvailable)
        {
            // tilføj ny task til db og knyt task til subproject
            projectService.insertNewTaskIntoDb(orgDbName, subprojectId, taskId,
                    createTaskViewModel);
        
            // fordi delprojektet oprettedes succesfuldt skal createTaskViewModel nu ikke vise indtastede titel
            // mere
            createTaskViewModel = null;
        
            // vi ryger tilbage til editProject
            return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
        }
    
        // Task'en er IKKE blevet gemt i databasen og guider derfor til samme GetMapping, hvor ugyldig title vises
        // via createTaskViewModel
    
        return String.format("redirect:/%s/editProject/%d/subproject/%d/createTask/%d", orgDbName, projectId,
                subprojectId, taskId);
    }
    
    // tilføj subtask-KNAP
    @GetMapping("/editProject/{projectId}/subproject/{subprojectId}/task/{taskId}/createSubtask/{nextSubtaskId}")
    public String createSubtask(@PathVariable String orgDbName, @PathVariable int projectId,
                             @PathVariable int subprojectId, @PathVariable int taskId,
                             @PathVariable int nextSubtaskId, Model orgDbNameModel,
                             Model loggedInUserModel, Model joinedProjectListModel, Model projectModel,
                             Model projectIdModel, Model subprojectIdModel, Model taskIdModel,
                                Model nextSubprojectIdModel, Model nextTaskIdModel, Model nextSubtaskIdModel,
                                Model createSubtaskModel)
    {
        // modeller til sidebars + menubars
        orgDbNameModel.addAttribute("orgDbName", orgDbName);
        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectListModel.addAttribute("joinedProjectList", projectService.updateJoinedProjectList(orgDbName));
    
        // model til form-input-felt
        createSubtaskModel.addAttribute("createSubtaskViewModel", createSubtaskViewModel);
    
    
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
       
        
        // til form-knappers LINK
        nextSubprojectIdModel.addAttribute("nextSubprojectId", projectService.findNextIdFromTable(orgDbName, "subprojects"));
        nextTaskIdModel.addAttribute("nextTaskId", projectService.findNextIdFromTable(orgDbName, "tasks"));
        
    
    
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
                                    @PathVariable int subtaskId, WebRequest dataFromCreateSubtaskForm)
    {
        // opret CreateSubtaskViewModel(dataFromCreateSubtaskForm) ud fra webRequest
        createSubtaskViewModel = projectService.createSubtaskViewModelFromForm(dataFromCreateSubtaskForm);
    
    
        // tjek om tasktitel er optaget
        boolean subtaskTitleIsAvailable = projectService.isSubtaskTitleAvailable(orgDbName, taskId,
                createSubprojectViewModel.getTitle());
    
        // hvis subprojectTitle ikke allerede findes på projektet
        if(subtaskTitleIsAvailable)
        {
            // tilføj nyt subproject til db og knyt delprojekt til projektet
            projectService.insertNewSubtaskIntoDb(orgDbName, taskId, subtaskId,
                    createSubtaskViewModel);
        
            // fordi subtask oprettedes succesfuldt skal createSubtaskViewModel nu ikke vise indtastede titel
            // mere
            createSubtaskViewModel = null;
        
            // vi ryger tilbage til editProject
            return String.format("redirect:/%s/editProject/%d", orgDbName, projectId);
        }
        
    
        // Subtask er IKKE blevet gemt i db --> guide derfor til samme GetMapping, hvor ugyldig title vises
        // via createSubtaskViewModel
        return String.format("redirect:/%s/editProject/%d/subproject/%d/task/%d/createSubtask", orgDbName, projectId,
                subprojectId, taskId, subtaskId);
    }
   
    




}
