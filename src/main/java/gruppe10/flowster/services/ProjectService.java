package gruppe10.flowster.services;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.repositories.FlowsterRepository;
import gruppe10.flowster.repositories.ProjectRepository;
import gruppe10.flowster.viewModels.project.CreateProjectViewModel;
import gruppe10.flowster.viewModels.project.CreateSubViewModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;

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
    public CreateSubViewModel createSubprojectViewModelFromForm(WebRequest dataFromCreateSubprojectForm)
    {
        // alt input fra form kommer ud som String
        double manhours = 0;
        String manhoursString =  dataFromCreateSubprojectForm.getParameter("manhours");
        
        // derfor - hvis input ikke er null
        if(!manhoursString.equals(""))
        {
            // lav String om til double - vi behøver ikke sikre os at det der er i stringen kan konverteres til
            // double, - det har vi sørget for via form input type="number"
            manhours =  Double.parseDouble(manhoursString);
        }
        
        return new CreateSubViewModel
                       (dataFromCreateSubprojectForm.getParameter("title"), manhours);
    }
    
    public boolean isSubprojectTitleAvailable(String orgDbName, int projectId, String subprojectTitle)
    {
        return projectRepository.checkIfSubprojectTitleIsAvailable(orgDbName, projectId, subprojectTitle);
    }
    
    
    public boolean doSubrojectHoursExceedTaskHours(String orgDbName, int subprojectId, double manhours)
    {
        boolean subprojectHoursExceedTaskHours = true;
    
        // hvis der ikke er indtastet en værdi, så overstiger det IKKE delprojektets tidsramme
        if(manhours != 0)
        {
            double difference =
                    projectRepository.calcHoursDiffBetweenSubprojectAndTasks(orgDbName, subprojectId, manhours);
            
            // hvis difference == 0 projectTimer exceeder, men grænseværdi
            // if difference > 0 exceeder
            // if difference < 0 exceeder IKKE
    
            // returnerer true hvis subproject-timer er over/= tasks'
            // returnerer false hvis subproject-timer er under tasks'
            
            return difference >= 0;
        }
    
        return subprojectHoursExceedTaskHours;
    }
    
    public double calcHoursDiffBetweenSubprojectAndTasks(String orgDbName, int subprojectId, double manhours)
    {
        // finde difference i timer mellem subpro og tasks
        double diff = projectRepository.calcHoursDiffBetweenSubprojectAndTasks(orgDbName, subprojectId, manhours);
    
        // gør værdi positiv
        double posDiff = Math.abs(diff);
    
        System.out.println("posDiff: " + posDiff);
        
        // sørger for 1 decimal-tal
        double posDiffRounded = Math.round(posDiff * 100.0) / 100.0;
    
        System.out.println("posDiffRounded: " + posDiffRounded);
     
        return posDiffRounded;
    }
    
    
    public void insertNewSubprojectIntoDb(String orgDbName, int projectId, int subprojectId,
                                          CreateSubViewModel createSubViewModel)
    {
        // indsætter nyt subproject i subprojects-tabel
        projectRepository.insertNewSubprojectIntoDb(orgDbName, createSubViewModel);
        
        // tilknytter det nye subproject til projektet
        projectRepository.insertRowIntoProjectsSubprojects(orgDbName, projectId, subprojectId);
    }
    
    // CREATE TASK
    
    public boolean isTaskTitleAvailable(String orgDbName, int subprojectId, String taskTitle)
    {
        return projectRepository.checkIfTaskTitleIsAvailable(orgDbName, subprojectId, taskTitle);
    }
    
    public void insertNewTaskIntoDb(String orgDbName, int subprojectId, int taskId,
                                          CreateSubViewModel createSubViewModel)
    {
        // indsætter ny task i tasks-tabel
        projectRepository.insertNewTaskIntoDb(orgDbName, createSubViewModel.getTitle(), createSubViewModel.getManhours());
        
        // tilknytter den nye task til subproject'et
        projectRepository.insertRowIntoSubprojectsTasks(orgDbName, subprojectId, taskId);
    }
    
    
    public String retrieveSubprojectNameFromId(String dbName, int subprojectId)
    {
        return projectRepository.retrieveSubprojectTitleFromId(dbName, subprojectId);
    }
    
    // CREATE SUBTASK
    
    public boolean isSubtaskTitleAvailable(String orgDbName, int taskId, String subtaskTitle)
    {
        return projectRepository.checkIfSubtaskTitleIsAvailable(orgDbName, taskId, subtaskTitle);
    }
    
    public void insertNewSubtaskIntoDb(String orgDbName, int taskId, int subtaskId,
                                    CreateSubViewModel createSubViewModel)
    {
        // indsætter ny subtask i subtasks-tabel
        projectRepository.insertNewSubtaskIntoDb(orgDbName, createSubViewModel.getTitle(), createSubViewModel.getManhours());
        
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
