package gruppe10.flowster.services;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.repositories.FlowsterRepository;
import gruppe10.flowster.repositories.ProjectRepository;
import gruppe10.flowster.viewModels.project.CreateProjectViewModel;
import gruppe10.flowster.viewModels.project.CreateSubtaskViewModel;
import gruppe10.flowster.viewModels.project.CreateSubprojectViewModel;
import gruppe10.flowster.viewModels.project.CreateTaskViewModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

@Service
public class ProjectService
{
    FlowsterRepository flowsterRepository = new FlowsterRepository();
    ProjectRepository projectRepository = new ProjectRepository();

    public ArrayList<Project> updateJoinedProjectList(String dbName)
    {
        int currentLoggedInUserId =  UserService.loggedInUser.getId();
        
        // projekter som findes på de teams, loggedInUser er tilknyttet
        ArrayList<Project> joinedProjectList = projectRepository.retrieveProjectListFromUserId(dbName, currentLoggedInUserId);
        
        // hvis brugeren er en projectManager
        if(UserService.loggedInUser instanceof ProjectManager)
        {
            // hent de projekter som hun/han har oprettet
            ArrayList<Project> createdProjectList= projectRepository.retrieveCreatedProjectListFromUserId(dbName,
                    currentLoggedInUserId);
            if(joinedProjectList != null)
            {
                for(Project project : createdProjectList)
                {
                    if(ifProjectAlreadyNotOnList(joinedProjectList, project.getId()))
                    {
                        joinedProjectList.add(project);
                    }
                }
                Collections.sort(joinedProjectList);
            }
            else
            {
                joinedProjectList = createdProjectList;
            }
    
            
        }
        
        return joinedProjectList;
    }
    
    
    public boolean ifProjectAlreadyNotOnList(ArrayList<Project> projectList, int id)
    {
        
        for(Project project : projectList)
        {
            if(id == project.getId())
            {
                return false;
            }
        }
        
        return true;
    }
    
    
    // public ArrayList<Project> projectList
    

    /**
     * Konverterer organisationName til db-name
     *
     * @param organisationName organisationName som skal konverteres til db-name
     * @return String konverterede db-name
     * */
    public String convertOrganisationNameToDbName(String organisationName)
    {
        // laver underscore hvor der er mellemrum i orgName
        String convertedOrganisationName = organisationName.replaceAll(" ", "_");

        // laver til små bogstaver
        convertedOrganisationName = convertedOrganisationName.toLowerCase();

        return "flowster_" + convertedOrganisationName;
    }
    
    public CreateProjectViewModel createProjectViewModelFromForm(WebRequest dataFromCreateProjectForm)
    {
        return new CreateProjectViewModel
         (dataFromCreateProjectForm.getParameter("project-title"), dataFromCreateProjectForm.getParameter(
                 "project-deadline"));
    }
    
    public boolean isProjectTitleAvailable(String dbName, CreateProjectViewModel createProjectViewModel)
    {
       return projectRepository.checkIfProjectTitleIsAvailable(dbName, createProjectViewModel.getTitle());
    }
    
    public void insertNewProjectIntoDb(String dbName, CreateProjectViewModel createProjectViewModel)
    {
        projectRepository.insertNewProjectIntoDb(dbName, createProjectViewModel);
    }
    
    public int retrieveProjectIdFromProjectTitle(String dbName, String projectTitle)
    {
       return projectRepository.retrieveProjectIdFromProjectTitle(dbName, projectTitle);
    }
    
    public void attachCreatorToCreatedProject(String dbName, int projectId, int userId)
    {
        projectRepository.insertRowIntoProjectsUsers(dbName, projectId, userId);
    }
    
    public int findNextIdFromTable(String dbName, String tableName)
    {
        // + 1 fordi vi finder det sidst tilføjede id i tabel og skal have det potentielle NÆSTE id
        return projectRepository.findMaxIdFromTable(dbName, tableName) + 1;
    }
    
    public Project retrieveProject(String orgDbName, int projectId)
    {
        return projectRepository.retrieveProject(orgDbName, projectId);
    }
    
    // CREATE SUBPROJECT
    
    /**
     * Opretter en CreateSubprojectViewModel ud fra data i form
     *
     *
     * */
    public CreateSubprojectViewModel createSubprojectViewModelFromForm(WebRequest dataFromCreateSubprojectForm)
    {
        // alt input fra form kommer ud som String
        double manhours = 0;
        String manhoursString =  dataFromCreateSubprojectForm.getParameter("subproject-manhours");
        
        // derfor - hvis input ikke er null
        if(!manhoursString.equals(""))
        {
            // lav String om til double - vi behøver ikke sikre os at det der er i stringen kan konverteres til
            // double, - det har vi sørget for via form input type="number"
            manhours =  Double.parseDouble(manhoursString);
        }
        
        return new CreateSubprojectViewModel
                       (dataFromCreateSubprojectForm.getParameter("subproject-title"), manhours);
    }
    
    public boolean isSubprojectTitleAvailable(String orgDbName, int projectId, String subprojectTitle)
    {
        return projectRepository.checkIfSubprojectTitleIsAvailable(orgDbName, projectId, subprojectTitle);
    }
    
    
    public void insertNewSubprojectIntoDb(String orgDbName, int projectId, int subprojectId,
                                          CreateSubprojectViewModel createSubprojectViewModel)
    {
        // indsætter nyt subproject i subprojects-tabel
        projectRepository.insertNewSubprojectIntoDb(orgDbName, createSubprojectViewModel);
        
        // tilknytter det nye subproject til projektet
        projectRepository.insertRowIntoProjectsSubprojects(orgDbName, projectId, subprojectId);
    }
    
    // CREATE TASK
    
    public CreateTaskViewModel createTaskViewModelFromForm(WebRequest dataFromCreateTaskForm)
    {
        return new CreateTaskViewModel(dataFromCreateTaskForm.getParameter("task-title"));
    }
    
    public boolean isTaskTitleAvailable(String orgDbName, int subprojectId, String taskTitle)
    {
        return projectRepository.checkIfTaskTitleIsAvailable(orgDbName, subprojectId, taskTitle);
    }
    
    public void insertNewTaskIntoDb(String orgDbName, int subprojectId, int taskId,
                                          CreateTaskViewModel createTaskViewModel)
    {
        // indsætter ny task i tasks-tabel
        projectRepository.insertNewTaskIntoDb(orgDbName, createTaskViewModel.getTitle());
        
        // tilknytter den nye task til subproject'et
        projectRepository.insertRowIntoSubprojectsTasks(orgDbName, subprojectId, taskId);
    }
    
    // CREATE SUBTASK
    
    public CreateSubtaskViewModel createSubtaskViewModelFromForm(WebRequest dataFromCreateSubtaskForm)
    {
        return new CreateSubtaskViewModel(dataFromCreateSubtaskForm.getParameter("subtask-title"));
    }
    
    public boolean isSubtaskTitleAvailable(String orgDbName, int taskId, String subtaskTitle)
    {
        return projectRepository.checkIfSubtaskTitleIsAvailable(orgDbName, taskId, subtaskTitle);
    }
    
    public void insertNewSubtaskIntoDb(String orgDbName, int taskId, int subtaskId,
                                    CreateSubtaskViewModel createSubtaskViewModel)
    {
        // indsætter ny subtask i subtasks-tabel
        projectRepository.insertNewSubtaskIntoDb(orgDbName, createSubtaskViewModel.getTitle());
        
        // tilknytter den nye subtask til task'en
        projectRepository.insertRowIntoTasksSubtasks(orgDbName, taskId, subtaskId);
    }
    
    
    
    // TILFØJ/FJERN TEAM
    public void insertRowIntoTeamsProjects(String orgDbName, int teamId, int projectId)
    {
        projectRepository.insertRowIntoTeamsProjects(orgDbName, teamId, projectId);
    }
    
    public void deleteRowFromTeamsProjects(String orgDbName, int teamId, int projectId)
    {
        projectRepository.deleteRowFromTeamsProjects(orgDbName, teamId, projectId);
    }
    
    
    
    
    
}
