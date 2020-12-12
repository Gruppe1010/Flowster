package gruppe10.flowster.services;

import gruppe10.flowster.repositories.FlowsterRepository;
import gruppe10.flowster.repositories.ProjectRepository;
import gruppe10.flowster.repositories.TeamRepository;
import gruppe10.flowster.viewModels.project.CreateProjectViewModel;
import org.springframework.web.context.request.WebRequest;

public class ProjectService
{
    FlowsterRepository flowsterRepository = new FlowsterRepository();
    TeamRepository teamRepository = new TeamRepository();
    ProjectRepository projectRepository = new ProjectRepository();

    public void updateJoinedProjectsList()
    {

        String dbName = convertOrganisationNameToDbName
                (flowsterRepository.retrieveOrganisationNameFromEmail(UserService.loggedInUser.getEmail()));

        UserService.loggedInUser.setJoinedProjectsList(projectRepository.retrieveProjectsArrayListFromUserId(dbName,
                UserService.loggedInUser.getId()));
    }

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
         (dataFromCreateProjectForm.getParameter("title"), dataFromCreateProjectForm.getParameter("deadline"));
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


}
