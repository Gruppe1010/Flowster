package gruppe10.flowster.controllers.users;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
// generalisering af GetMappings for hele klassen:
@RequestMapping("/projectManager")
public class ProjectManagerController
{
    @GetMapping("/frontPage")
    public String frontPage()
    {
        
        return "ProjectManager/front-page"; // html
    }
    
  

}
