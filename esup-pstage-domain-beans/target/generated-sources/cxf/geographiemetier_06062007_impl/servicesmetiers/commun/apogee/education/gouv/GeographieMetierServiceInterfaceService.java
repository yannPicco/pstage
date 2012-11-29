package geographiemetier_06062007_impl.servicesmetiers.commun.apogee.education.gouv;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.0
 * 2012-11-28T15:25:20.294+01:00
 * Generated source version: 2.7.0
 * 
 */
@WebServiceClient(name = "GeographieMetierServiceInterfaceService", 
                  wsdlLocation = "file:/home/pstage/workspace/esup-pstage/esup-pstage-domain-beans/src/main/resources/META-INF/wsdl/GeographieMetier.wsdl",
                  targetNamespace = "gouv.education.apogee.commun.servicesmetiers.GeographieMetier_06062007-impl") 
public class GeographieMetierServiceInterfaceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("gouv.education.apogee.commun.servicesmetiers.GeographieMetier_06062007-impl", "GeographieMetierServiceInterfaceService");
    public final static QName GeographieMetier = new QName("gouv.education.apogee.commun.servicesmetiers.GeographieMetier_06062007-impl", "GeographieMetier");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/pstage/workspace/esup-pstage/esup-pstage-domain-beans/src/main/resources/META-INF/wsdl/GeographieMetier.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(GeographieMetierServiceInterfaceService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/home/pstage/workspace/esup-pstage/esup-pstage-domain-beans/src/main/resources/META-INF/wsdl/GeographieMetier.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public GeographieMetierServiceInterfaceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public GeographieMetierServiceInterfaceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public GeographieMetierServiceInterfaceService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns GeographieMetierServiceInterface
     */
    @WebEndpoint(name = "GeographieMetier")
    public GeographieMetierServiceInterface getGeographieMetier() {
        return super.getPort(GeographieMetier, GeographieMetierServiceInterface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns GeographieMetierServiceInterface
     */
    @WebEndpoint(name = "GeographieMetier")
    public GeographieMetierServiceInterface getGeographieMetier(WebServiceFeature... features) {
        return super.getPort(GeographieMetier, GeographieMetierServiceInterface.class, features);
    }

}