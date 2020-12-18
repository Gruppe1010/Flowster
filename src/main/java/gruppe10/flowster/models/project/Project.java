package gruppe10.flowster.models.project;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.models.users.ProjectManager;
import gruppe10.flowster.models.users.User;

import javax.swing.text.DateFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Project implements Comparable<Project>
{
    private int id;
    private String title;
    private String description;
    private Date deadline;
    private double manhours;
    private User creator;
    private ArrayList<Subproject> subprojectList;
    private ArrayList<Team> teamList;
    
    // constructors
    public Project(int id)
    {
        this.id = id;
    }
    
    
    
    public Project(int id, String title, String description, Date deadline, double manhours, User creator,
                   ArrayList<Subproject> subprojectList, ArrayList<Team> teamList)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        if(manhours == 0)
        {
            this.manhours = calculateManhours(subprojectList);
        }
        else
        {
            this.manhours = manhours;
        }
        this.creator = creator;
        this.subprojectList = subprojectList;
        this.teamList = teamList;
    }
    
    
    // getters + setters
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public Date getDeadline()
    {
        return deadline;
    }
    public void setDeadline(Date deadline)
    {
        this.deadline = deadline;
    }
    public double getManhours()
    {
        return manhours;
    }
    public void setManhours(double manhours)
    {
        this.manhours = manhours;
    }
    public User getCreator()
    {
        return creator;
    }
    public void setCreator(User creator)
    {
        this.creator = creator;
    }
    public ArrayList<Subproject> getSubprojectList()
    {
        return subprojectList;
    }
    public void setSubprojectList(ArrayList<Subproject> subprojectList)
    {
        this.subprojectList = subprojectList;
    }
    public ArrayList<Team> getTeamList()
    {
        return teamList;
    }
    public void setTeamList(ArrayList<Team> teamList)
    {
        this.teamList = teamList;
    }
    
    
    
    public double calculateManhours(ArrayList<Subproject> subprojectList)
    {
        double manhours = 0;
        
        if(subprojectList != null && subprojectList.size() > 0)
        {
            // for hvert subprojekt
            for(Subproject subproject : subprojectList)
            {
                // tilføj subprojektets manhours til manhours der skal returneres
                manhours += subproject.getManhours();
            }
        }
        
        return manhours;
        
    }
    
    
    public String findSubHeadline()
    {
        String headline = "";
        
        if(deadline != null)
        {
            headline += "Deadline " + findFormattedDeadline();
        }
        
        if(manhours != 0)
        {
            headline += " - Estimeret timer " + manhours;
        }
    
        // System.out.println(calculateManhoursPrDay());
        
        return headline; // + " " + calculateManhoursPrDay();
    }
    
    
    public String findFormattedDeadline()
    {
        if(deadline == null)
        {
            return "";
        }
        
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        
        String formattedDate = dateFormatter.format(deadline);
    
        System.out.println(formattedDate);
        
        return formattedDate;
    }
    
    public double calculateManhoursPrDay()
    {
        double manhoursPrDay = 0;
        
        // udregn det kun hvis det BÅDE er en deadline OG angivede arbejdstimer
        if(deadline != null && manhours != 0)
        {
    
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String deadlineString = findFormattedDeadline().toString();
            
            LocalDateTime today = LocalDateTime.now();
            
            System.out.println(deadlineString);
            
            LocalDateTime dead = LocalDateTime.parse(deadlineString, dtf);
    
            long daysBetween = Duration.between(dead, today).toDays();
            System.out.println ("Days: " + daysBetween);
            
            manhoursPrDay = manhours / daysBetween;
            
        }
        
        return manhoursPrDay;
    }
    
    
    @Override
    public int compareTo(Project project)
    {
        if(id > project.id)
        {
            return 1; //hvis this.obj > param.obj
        }
        else if(id < project.id)
        {
            return -1; // hvis this.obj < param.obj
        }
  
        return 0; // this.obj == param.obj (dette vil dog ALDRIG ske, fordi vores id_project er UNIQUE)
    }
    

    
    
    
    
    
}


