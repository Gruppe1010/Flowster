package gruppe10.flowster.services;

import gruppe10.flowster.models.project.Project;
import gruppe10.flowster.repositories.FlowsterRepository;
import gruppe10.flowster.repositories.ProjectRepository;
import gruppe10.flowster.viewModels.project.CreateProjectViewModel;
import gruppe10.flowster.viewModels.project.CreateSubprojectViewModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

@Service
public class ProjectService
{
    FlowsterRepository flowsterRepository = new FlowsterRepository();
    ProjectRepository projectRepository = new ProjectRepository();

    public ArrayList<Project> updateJoinedProjectList(String dbName)
    {
        int currentLoggedInUserId =  UserService.loggedInUser.getId();
        
        // projekter som findes på de teams, loggedInUser er tilknyttet
        ArrayList<Project> projectList = projectRepository.retrieveProjectListFromUserId(dbName, currentLoggedInUserId);
        
        // hvis brugeren er en projectManager
        if(UserService.loggedInUser.isProjectManager())
        {
            // hent de projekter som hun/han har oprettet
            ArrayList<Project> createdProjectList= projectRepository.retrieveCreatedProjectListFromUserId(dbName,
                    currentLoggedInUserId);
            
            // hvis den ENE ikke er null
            if(projectList == null && createdProjectList != null)
            {
                projectList = createdProjectList;
            }
            // hvis begge IKKE er null, skal de sammenlægges
            else if(createdProjectList != null && projectList != null)
            {
                // tilføj disse (hvis disse IKKE er dupliketter af et projekt på listen) til listen
                projectList.addAll(createdProjectList);
            }
        }
        
        return projectList;
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
    


}
