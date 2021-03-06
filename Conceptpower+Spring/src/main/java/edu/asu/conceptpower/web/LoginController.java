package edu.asu.conceptpower.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class provides methods for login ,logout and login error operation
 * 
 * @author Chetan and Julia Damerow
 * 
 */
@Controller
public class LoginController {

    /**
     * This method redirects user to home page for use login
     * 
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean) {
        return "welcome";
    }

    /**
     * This method sets error message in home page when there are any errros
     * with user login
     * 
     * @param model
     *            A generic model holder for Servlet
     */
    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public String loginerror(ModelMap model, @ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean) {
        model.addAttribute("show_error_alert", true);
        model.addAttribute("error_alert_msg", "Login failed. Please enter correct username and password.");
        return "welcome";
    }

    /**
     * This method redirect users to homepage when user logs out
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@ModelAttribute("conceptSearchBean") ConceptSearchBean conceptSearchBean) {
        return "welcome";
    }

}