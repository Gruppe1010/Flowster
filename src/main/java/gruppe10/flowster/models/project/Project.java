package gruppe10.flowster.models.project;

import gruppe10.flowster.models.teams.Team;
import gruppe10.flowster.models.users.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
//
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
    
    // ANDRE metoder
    public String findSubHeadline()
    {
        String headline = "";
        
        if(deadline != null)
        {
            headline += "Deadline " + findFormattedDeadline();
        }
        if(manhours != 0)
        {
            headline += " - Estimerede timer " + manhours;
        }
        double manhoursPrDay = calculateManhoursPrDay();
        
        if(manhoursPrDay != 0)
        {
            headline += " - Arbejdstimer på projekt pr. dag " + manhoursPrDay;
        }
        
        return headline;
    }
    
    
    public String findFormattedDeadline()
    {
        if(deadline == null)
        {
            return "";
        }
        
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        
        String formattedDate = dateFormatter.format(deadline);
        
        return formattedDate;
    }
    
    /**
     * Sammenlægger arbejdstimer fra alle delprojekter der tages imod som parameter
     *
     * @param subprojectList Liste af delprojekter vis arbejdstimer lægges sammen
     * @return double: sammenlagte timer fra delprojekter
     * */
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
    
    public double calculateManhoursPrDay()
    {
        double manhoursPrDay = 0;
        
        // udregn det kun hvis der BÅDE er en deadline OG angivede arbejdstimer
        if(deadline != null && manhours != 0)
        {
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date currentDate = new Date();
            
            if(currentDate.before(deadline))
            {
                long diff = deadline.getTime() - currentDate.getTime();
                double daysBetween = (double) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) + 1;
    
                manhoursPrDay = manhours/daysBetween;
    
                // fx: 33.3333 * 100 == 3333  -->  3333/ 100 == 33.33
                manhoursPrDay = Math.round(manhoursPrDay * 100.0) / 100.0;
            }
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


