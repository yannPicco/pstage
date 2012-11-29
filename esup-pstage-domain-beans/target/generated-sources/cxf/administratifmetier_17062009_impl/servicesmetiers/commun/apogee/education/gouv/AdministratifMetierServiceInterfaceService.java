package administratifmetier_17062009_impl.servicesmetiers.commun.apogee.education.gouv;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.0
 * 2012-11-28T15:25:31.163+01:00
 * Generated source version: 2.7.0
 * 
 */
@WebServiceClient(name = "AdministratifMetierServiceInterfaceService", 
                  wsdlLocation = "file:/home/pstage/workspace/esup-pstage/esup-pstage-domain-beans/src/main/resources/META-INF/wsdl/AdministratifMetier.wsdl",
                  targetNamespace = "gouv.education.apogee.commun.servicesmetiers.AdministratifMetier_17062009-impl") 
public class AdministratifMetierServiceInterfaceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("gouv.education.apogee.commun.servicesmetiers.AdministratifMetier_17062009-impl", "AdministratifMetierServiceInterfaceService");
    public final static QName AdministratifMetier = new QName("gouv.education.apogee.commun.servicesmetiers.AdministratifMetier_17062009-impl", "AdministratifMetier");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/pstage/workspace/esup-pstage/esup-pstage-domain-beans/src/main/resources/META-INF/wsdl/AdministratifMetier.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(AdministratifMetierServiceInterfaceService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/home/pstage/workspace/esup-pstage/esup-pstage-domain-beans/src/main/resources/META-INF/wsdl/AdministratifMetier.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public AdministratifMetierServiceInterfaceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public AdministratifMetierServiceInterfaceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AdministratifMetierServiceInterfaceService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns AdministratifMetierServiceInterface
     */
    @WebEndpoint(name = "AdministratifMetier")
    public AdministratifMetierServiceInterface getAdministratifMetier() {
        return super.getPort(AdministratifMetier, AdministratifMetierServiceInterface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AdministratifMetierServiceInterface
     */
    @WebEndpoint(name = "AdministratifMetier")
    public AdministratifMetierServiceInterface getAdministratifMetier(WebServiceFeature... features) {
        return super.getPort(AdministratifMetier, AdministratifMetierServiceInterface.class, features);
    }

}