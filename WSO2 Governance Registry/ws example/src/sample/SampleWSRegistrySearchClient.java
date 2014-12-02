package sample;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.wso2.carbon.base.ServerConfiguration;
import org.wso2.carbon.governance.api.generic.GenericArtifactManager;
import org.wso2.carbon.governance.api.generic.dataobjects.GenericArtifact;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.governance.client.WSRegistrySearchClient;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.pagination.PaginationContext;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleWSRegistrySearchClient {

    private static ConfigurationContext configContext = null;

    private static final String CARBON_HOME = ".." + File.separator + ".." + File.separator;
    private static final String axis2Repo = CARBON_HOME + File.separator + "repository" +
            File.separator + "deployment" + File.separator + "client";
    private static final String axis2Conf = ServerConfiguration.getInstance().getFirstProperty("Axis2Config.clientAxis2XmlLocation");
    private static final String username = "admin";
    private static final String password = "admin";
    private static final String serverURL = "https://localhost:9443/services/";
    private static Registry registry;

    private static WSRegistryServiceClient initialize() throws Exception {

        System.setProperty("javax.net.ssl.trustStore", CARBON_HOME + File.separator + "repository" +
                File.separator + "resources" + File.separator + "security" + File.separator +
                "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("carbon.repo.write.mode", "true");
        configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                axis2Repo, axis2Conf);
        return new WSRegistryServiceClient(serverURL, username, password, configContext);

    }

    public static void main(String[] args) throws Exception {
        try {
            registry = initialize();
            Registry gov = GovernanceUtils.getGovernanceUserRegistry(registry, "admin");
            // Should be load the governance artifact.
            GovernanceUtils.loadGovernanceArtifacts((UserRegistry) gov);
            //Initialize the pagination context.
            PaginationContext.init(0, 20, "", "", 10);
            WSRegistrySearchClient wsRegistrySearchClient =
                    new WSRegistrySearchClient(serverURL, username, password, configContext);
            //This should be execute to initialize the AttributeSearchService.
            wsRegistrySearchClient.init();
            //Initialize the GenericArtifactManager
            GenericArtifactManager artifactManager =
                    new GenericArtifactManager(gov, "service");
            Map<String, List<String>> listMap = new HashMap<String, List<String>>();
            //Create the search attribute map
            listMap.put("lcName", new ArrayList<String>() {{
                add("ServiceLifeCycle");
            }});
            listMap.put("lcState", new ArrayList<String>() {{
                add("Development");
            }});
            //Find the results.
            GenericArtifact[] genericArtifacts = artifactManager.findGenericArtifacts(listMap);

            for(GenericArtifact artifact : genericArtifacts){
                System.out.println(artifact.getPath());
            }

        } finally {
            PaginationContext.destroy();
            ((WSRegistryServiceClient)registry).logut();
            System.exit(0);
        }

    }
}
