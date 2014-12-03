package sample.model.impl;

import sample.model.Action;
import sample.model.Action1;


/**
 * Default implementation for {@link Action}
 * @author bgmpa
 *
 */
public class Action1Impl extends ActionImpl
		implements Action1 {
    private String customerProject;
    
    public String getCustomerProject() {
        return customerProject;
    }

    public void setCustomerProject(String pr) {
        customerProject = pr;
    }

}
