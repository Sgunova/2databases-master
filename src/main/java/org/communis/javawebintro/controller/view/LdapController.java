package org.communis.javawebintro.controller.view;


import org.communis.javawebintro.dto.LdapAuthWrapper;
import org.communis.javawebintro.dto.filters.LdapAuthFilterWrapper;
import org.communis.javawebintro.enums.UserRole;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.service.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/admin/ldap")
public class LdapController {

    private String LDAP_VIEW_PATH = "admin/ldap/";

    private final LdapService ldapService;

    @Autowired
    public LdapController(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView getLdapListPage(Pageable pageable, LdapAuthFilterWrapper filter) throws ServerException {
        ModelAndView ldapListPage = new ModelAndView(LDAP_VIEW_PATH + "list");
        ldapListPage.addObject("page", ldapService.getPageByFilter(pageable, filter));
        ldapListPage.addObject("filter", filter);
        return ldapListPage;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {
        ModelAndView addPage = new ModelAndView(LDAP_VIEW_PATH + "add");
        addPage.addObject("ldap", new LdapAuthWrapper());
        addPage.addObject("roles", UserRole.values());
        return addPage;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Long id) throws ServerException {
        ModelAndView addPage = new ModelAndView(LDAP_VIEW_PATH + "edit");
        addPage.addObject("ldap", ldapService.getForEdit(id));
        addPage.addObject("roles", UserRole.values());
        return addPage;
    }
}
