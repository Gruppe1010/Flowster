package gruppe10.flowster.controllers;

import gruppe10.flowster.models.users.User;
import gruppe10.flowster.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{orgDbName}")
public class ProjectController
{
    UserService userService = new UserService();

    @GetMapping("/projects")
    public String projects(@PathVariable String orgDbName, Model orgDbNameModel, Model loggedInUserModel, Model joinedProjectsList)
    {

        orgDbName = userService.findOrgDbName();
        orgDbNameModel.addAttribute("orgDbName", orgDbName);

        loggedInUserModel.addAttribute("loggedInUser", UserService.loggedInUser);
        joinedProjectsList.addAttribute("joinedProjectsList", joinedProjectsList);

        return "project/projects"; // html
    }







}
