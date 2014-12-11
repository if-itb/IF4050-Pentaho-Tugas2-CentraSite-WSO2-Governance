package org.wso2.carbon.registry.samples.services;
  
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.services.ServiceFilter;
import org.wso2.carbon.governance.api.services.ServiceManager;
import org.wso2.carbon.governance.api.services.dataobjects.Service;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
  
import java.util.LinkedList;
import java.util.List;
  
public class EndpointLookupService {
  
    private Registry governanceRegistry;
  
    public EndpointLookupService() {
        try {
            governanceRegistry = EndpointLookupServiceComponent.getRegistryService()
                    .getGovernanceSystemRegistry();
        } catch (RegistryException e) {
            e.printStackTrace();
        }
    }
  
    public String[] getEndpoints(final String serviceName, final String serviceNamespace,
                                 final String version, final String environment)
            throws GovernanceException, RegistryException {
        ServiceManager manager = new ServiceManager(governanceRegistry);
        Service[] services = manager.findServices(new ServiceFilter() {
            public boolean matches(Service service) throws GovernanceException {
                return serviceName.equals(service.getQName().getLocalPart()) &&
                        (serviceNamespace == null || serviceNamespace.length() == 0 ||
                                serviceNamespace.equals(service.getQName().getNamespaceURI())) &&
                        (version == null || version.length() == 0 ||
                                version.equals(service.getAttribute("overview_version")));
            }
        });
        if (services != null && services.length > 0) {
            List<String> endpoints = new LinkedList<String>();
            for (Service service : services) {
                String[] entries = service.getAttributes("endpoints_entry");
                if (entries != null && entries.length > 0) {
                    for (String entry : entries) {
                        int idx = entry.indexOf(":");
                        if (entry.substring(0, idx).equals(environment)) {
                            endpoints.add(entry.substring(idx + 1));
                        }
                    }
                }
            }
            return endpoints.toArray(new String[endpoints.size()]);
        }
        return new String[0];
    }
}