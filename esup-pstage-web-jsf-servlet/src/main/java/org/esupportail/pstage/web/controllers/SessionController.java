/**
 * ESUP-PStage - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-pstage
 */
package org.esupportail.pstage.web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.controllers.ExceptionController;
import org.esupportail.pstage.domain.beans.NousContacter;
import org.esupportail.pstage.domain.beans.User;
import org.esupportail.pstage.services.authentication.Authenticator;
import org.esupportail.pstage.utils.Utils;
import org.esupportail.pstage.web.beans.FileUploadBean;
import org.esupportail.pstage.web.beans.ImageUploadBean;
import org.esupportail.pstagedata.domain.dto.AdminStructureDTO;
import org.esupportail.pstagedata.domain.dto.CentreGestionDTO;
import org.esupportail.pstagedata.domain.dto.ContactDTO;
import org.esupportail.pstagedata.domain.dto.DroitAdministrationDTO;
import org.esupportail.pstagedata.domain.dto.EnseignantDTO;
import org.esupportail.pstagedata.domain.dto.EtudiantDTO;
import org.esupportail.pstagedata.domain.dto.PersonnelCentreGestionDTO;
import org.esupportail.pstagedata.domain.dto.StructureDTO;
/**
 * A bean to memorize the context of the application.
 */
/**
 * @author Matthieu Manginot : matthieu.manginot@univ-nancy2.fr
 *
 */
public class SessionController extends AbstractDomainAwareBean {

	/* ***************************************************************
	 * Propriétés
	 ****************************************************************/
	/**
	 * Logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass());
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5936434246704000653L;
	/**
	 * The exception controller (called when logging in/out).
	 */
	private ExceptionController exceptionController;	
	/**
	 * The authenticator.
	 */
	private Authenticator authenticator;	
	/**
	 * true to print login/logout button in servlet mode.
	 */
	private boolean printLoginLogoutButtons = true;
	/**
	 * The CAS logout URL.
	 */
	private String casLogoutUrl;
	/**
	 * NousContacter
	 */
	private NousContacter nousContacter=new NousContacter();
	/**
	 * Nom donnée à l'application Entreprise
	 */
	private String applicationNameEntreprise;
	/**
	 * Url pour l'accès à l'espace entreprise (utilisé dans les mails)
	 */
	private String entrepriseUrl;
	/**
	 * 
	 */
	private String baseUrl;
	/**
	 * Liste de diffusion Entreprise
	 */
	private String mailingListEntr;
	/**
	 * Envoi d'un mail à l'ajout d'un etab
	 */
	private boolean mailingListEntrMailAvertissementAjoutEtab;
	/**
	 * Envoi d'un mail à la modif d'un etab
	 */
	private boolean mailingListEntrMailAvertissementModifEtab;
	/**
	 * Envoi d'un mail à la suppression d'un etab
	 */
	private boolean mailingListEntrMailAvertissementSupprEtab;
	/**
	 * Envoi d'un mail à l'ajout d'une offre par une entreprise ou admin
	 */
	private boolean mailingListEntrMailAvertissementAjoutOffre;
	/**
	 * Envoi d'un mail à la modification d'une offre par une entreprise ou admin
	 */
	private boolean mailingListEntrMailAvertissementModifOffre;
	/**
	 * Envoi d'un mail à la suppression d'une offre par une entreprise ou admin
	 */
	private boolean mailingListEntrMailAvertissementSupprOffre;
	
	/**
	 * InternetAddress
	 */
	private InternetAddress mailingListEntrIA;
	/**
	 * Mode d'authentification des administrateurs de l'espace entreprise : cas OU shibb
	 */
	private String adminAuthentication;
	/**
	 * Propriété init.superAdmin du fichier config, séparés par ";" si plusieurs 
	 */
	private String superAdmin;
	/**
	 * Tableau des super administrateurs
	 */
	private String[] superAdminTab;
	/**
	 * Administrateur actuellement connecté
	 */
	private AdminStructureDTO currentAuthAdminStructure;
	/**
	 * Contact actuellement connecté
	 */
	private ContactDTO currentAuthContact;
	/**
	 * Structure actuellement gérée
	 * -> soit gestion par un administrateur
	 * -> soit consultation côté Stage
	 * -> soit structure du contact actuellement connecté
	 */
	private StructureDTO currentManageStructure;
	/**
	 * True si menu de gestion affiché, false menu non affiché
	 */
	private boolean menuGestionEtab=false;
	/**
	 * Personnel actuellement connecté
	 */
	private PersonnelCentreGestionDTO currentAuthPersonnel;
	/**
	 * Enseignant actuellement connecté
	 */
	private EnseignantDTO currentAuthEnseignant;
	/**
	 * Étudiant actuellement connecté
	 */
	private EtudiantDTO currentAuthEtudiant;
	/**
	 * Centre(s) de gestion au(x)quel(s) la personne actuellement connectée est rattachée 
	 */
	private List<CentreGestionDTO> currentCentresGestion;
	/**
	 * User courant via Cas pour Stage
	 */
	private User currentStageCasUser;
	/**
	 * Login de la personne actuellement connecté
	 * (voir get)
	 */
	private String currentLogin;
	/**
	 * vrai si la personne actuellement connectée par CAS ou Shibb n'est pas autorisée
	 * à administrer l'espace Entreprise
	 */
	private boolean notAdminEntrepriseViaCasShibb=false;	
	/**
	 * Nom donnée à l'application PStage
	 */
	private String applicationNamePStage;
	/**
	 * Liste de diffusion PStage
	 */
	private String mailingListPStage;
	/**
	 * InternetAddress
	 */
	private InternetAddress mailingListPStageIA;
	/**
	 * Classe d'upload d'image avec redimensionnement
	 */
	private ImageUploadBean imageUploadBean;
	/**
	 * Classe d'upload de fichiers pour les offres
	 */
	private FileUploadBean offreFileUploadBean;
	/**
	 * uploadFilesFileSizeLimit
	 */
	private String uploadFilesFileSizeLimit;
	/**
	 * uploadImagesFileSizeLimit
	 */
	private String uploadImagesFileSizeLimit;
	/**
	 * Extensions de fichiers autorisées pour les offres
	 */
	private String uploadFilesOffresFileExtensions;
	/**
	 * Extensions de fichiers autorisées pour les logo de centres
	 */
	private String uploadImagesFileExtensions;
	/**
	 * Répertoire des logos des entreprises
	 */
	private String uploadFilesLogosEntreprisePath;
	/**
	 * Répertoire des fichiers attachées aux offres
	 */
	private String uploadFilesOffresPath;
	/**
	 * Répertoire des logos des centres de gestion
	 */
	private String uploadFilesLogosCentrePath;
	/**
	 * Vrai si récupération des communes françaises via Apogée
	 */
	private boolean recupererCommunesDepuisApogee=false;
	/**
	 * Autoriser ou non la création d'une convention par les étudiants dont l'ETAPE/UFR n'est gérée pas aucun centre
	 */
	private boolean autoriserConventionsOrphelines=true;
	/**
	 * Autoriser ou non les centres de gestion à bloquer l'impression de la convention par les étudiants
	 */
	private boolean autoriserCentresBloquerImpressionConvention=false;
	/**
	 * Autoriser ou non les entreprises à réserver les offres aux centres de gestion
	 */
	private boolean autoriserEntrepriseAReserverOffre=false;
	/**
	 * Autoriser ou non les étudiants à modifier les données entreprise (fiche signalétique, services, contacts) 
	 * lorsqu'il créent une convention 
	 */
	private boolean autoriserEtudiantAModifierEntreprise=true;
	/**
	 * Code de l'université récupéré depuis le fichier config.properties grace à la propriété du bean CentreController
	 */
	private String codeUniversite;
	/**
	 * centre gestion rattachement pour convention, contact suivant ufr, etape
	 */
	private CentreGestionDTO centreGestionRattachement;
	/**
	 * Map contenant les droits d'acces du personnel en fonction de l'id d'un centre
	 */
	private Map<Integer, DroitAdministrationDTO> droitsAccesMap = new HashMap<Integer,DroitAdministrationDTO>();
	
	/**
	 * True if simulate portlet mode.
	 */
	private Boolean isEnt;

	/**
	 * Critere vide, UFR ou ETAPE récupéré depuis le fichier config.properties
	 */
	private String critereGestion;
	
	/**
	 * Vrai si Espace Entreprise - l'entreprise peut acceder l'application CVtheque
	 */
	private boolean cvtheque;
	
	/**
	 * si cvtheque=true - url de l'application CVtheque
	 */
	private String cvthequeUrl;
	
	/**
	 * Vrai si url assistance
	 */
	private boolean assistance;
	
	/**
	 * url assistance
	 */
	private String urlAssistance;
	
	/**
	 * vrai si Enseignant Tuteur
	 */
	private boolean enseignantTuteur=false;

	/**
	 * vrai si les gestionnaires doivent moderer les offres
	 */
	private boolean moderationActive;
	/**
	 * Constructor.
	 */
	public SessionController() {
		super();
	}
	
	/**
	 * @see org.esupportail.pstage.web.controllers.AbstractDomainAwareBean#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		isEnt = true;
	}
	
	/* ***************************************************************
	 * Actions
	 ****************************************************************/

	/**
	 * @see org.esupportail.pstage.web.controllers.AbstractDomainAwareBean#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		Assert.notNull(this.exceptionController, "property exceptionController of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.authenticator, "property authenticator of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.mailingListPStage, "property mailingListPStage of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.codeUniversite, "property universityCode of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.baseUrl, "property baseUrl of class " 
				+ this.getClass().getName() + " can not be null");
		if(org.springframework.util.StringUtils.hasText(mailingListEntr)){
			try {
				if(logger.isDebugEnabled()){
					logger.debug("this.mailingListEntr = "+this.mailingListEntr);
				}
				this.mailingListEntrIA = new InternetAddress(this.mailingListEntr);
				this.mailingListEntrIA.validate();
			} catch (AddressException e) {
				e.printStackTrace();
				Assert.isTrue(false, "with property mailingListEntr = "+this.mailingListEntr);
			}
		}
		if(org.springframework.util.StringUtils.hasText(mailingListPStage)){
			try {
				if(logger.isDebugEnabled()){
					logger.debug("this.mailingListPStage = "+this.mailingListPStage);
				}
				this.mailingListPStageIA = new InternetAddress(this.mailingListPStage);
				this.mailingListPStageIA.validate();
			} catch (AddressException e) {
				e.printStackTrace();
				Assert.isTrue(false, "with property mailingListPStage = "+this.mailingListPStage);
			}
		}
		if(org.springframework.util.StringUtils.hasText(superAdmin)){
			this.superAdminTab = this.superAdmin.split(";");
		}else{
			logger.warn("Property superAdmin (-> init.superAdmin) of class " 
				+ this.getClass().getName() + " can not be null : "+superAdmin);
		}
		if(org.springframework.util.StringUtils.hasText(adminAuthentication)){
			if(!adminAuthentication.equals("cas") && !adminAuthentication.equals("shibb")){
				Assert.isTrue(false, "property adminAuthentication must be 'cas' or 'shibb'");
			}
		}
		Assert.isTrue(this.uploadFilesOffresPath!= null && !this.uploadFilesOffresPath.isEmpty(), "property uploadFilesOffresPath of class " 
				+ this.getClass().getName() + " can not be null");	
		this.offreFileUploadBean=new FileUploadBean(this.uploadFilesOffresPath);
		Assert.notNull(this.uploadFilesFileSizeLimit, "property uploadFilesFileSizeLimit of class " 
				+ this.getClass().getName() + " can not be null");
		if(Utils.isNumber(uploadFilesFileSizeLimit)){
			int i = Utils.convertStringToInt(this.uploadFilesFileSizeLimit);
			if((""+i).length()<=3 && (""+i).length()>1){
				this.uploadFilesFileSizeLimit=i+"o";
			}
			i = i /1024;
			if((""+i).length()<=3 && (""+i).length()>1){
				this.uploadFilesFileSizeLimit=i+"ko";
			}
			i = i /1024;
			if((""+i).length()<=3 && (""+i).length()>1){
				this.uploadFilesFileSizeLimit=i+"mo";
			}
		}
		
		Assert.isTrue(this.uploadFilesLogosCentrePath!= null && !this.uploadFilesLogosCentrePath.isEmpty(), "property uploadFilesLogosCentrePath of class " 
				+ this.getClass().getName() + " can not be null");	
		this.imageUploadBean=new ImageUploadBean(this.uploadFilesLogosCentrePath);
		Assert.notNull(this.uploadImagesFileSizeLimit, "property uploadImagesFileSizeLimit of class " 
				+ this.getClass().getName() + " can not be null");
		if(Utils.isNumber(uploadImagesFileSizeLimit)){
			int i = Utils.convertStringToInt(this.uploadImagesFileSizeLimit);
			if((""+i).length()<=3 && (""+i).length()>1){
				this.uploadImagesFileSizeLimit=i+"o";
			}
			i = i /1024;
			if((""+i).length()<=3 && (""+i).length()>1){
				this.uploadImagesFileSizeLimit=i+"ko";
			}
			i = i /1000;
			if((""+i).length()<=3 && (""+i).length()>1){
				this.uploadImagesFileSizeLimit=i+"mo";
			}
		}
		
		Assert.notNull(this.uploadFilesOffresFileExtensions, "property uploadFilesOffresFileExtensions of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.uploadImagesFileExtensions, "property uploadImagesFileExtensions of class " 
				+ this.getClass().getName() + " can not be null");
		
	}

	/**
	 * @return the current user, or null if guest.
	 */
	@Override
	public User getCurrentUser() {
		return authenticator.getUser();
	}
	
	/**
	 * @return true if the login button should be printed. 
	 */
	public boolean isPrintLogin() {
		if (!printLoginLogoutButtons) {
			return false;
		}
		return getIsServlet()&& getCurrentUser() == null;
	}
	
	/**
	 * @return true if the logout button should be printed. 
	 */
	public boolean isPrintLogout() {
		if (!printLoginLogoutButtons) {
			return false;
		}
		
		return getIsServlet()&& getCurrentUser() != null; 
	}
	
	/**
	 * @return true if this application is mode servlet. 
	 */
	public boolean getIsServlet() {
		if (isEnt) {
			return false;
		}
		return ContextUtils.isServlet();
	}

	
	/**
	 * @return a debug String.
	 */
	public String getDebug() {
		return toString();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getName() + "#" + hashCode();
	}	

	/**
	 * JSF callback.
	 * @return a String.
	 * @throws IOException 
	 */
	public String logout() throws IOException {
		if (ContextUtils.isPortlet()) {
			throw new UnsupportedOperationException("logout() should not be called in portlet mode.");
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String returnUrl = request.getRequestURL().toString().replaceFirst("/stylesheets/[^/]*$", "");
		String forwardUrl;
		Assert.hasText(
				casLogoutUrl, 
				"property casLogoutUrl of class " + getClass().getName() + " is null");
		forwardUrl = String.format(casLogoutUrl, StringUtils.utf8UrlEncode(returnUrl));
		// note: the session beans will be kept even when invalidating 
		// the session so they have to be reset (by the exception controller).
		// We invalidate the session however for the other attributes.
		request.getSession().invalidate();
		request.getSession(true);
		// calling this method will reset all the beans of the application
		exceptionController.restart();
		externalContext.redirect(forwardUrl);
		facesContext.responseComplete();
		return null;
	}
	
	
	/**
	 * @param logoutEnt
	 */
	public void logoutEnt(Boolean logoutEnt)  {
		if (ContextUtils.isPortlet()) {
			throw new UnsupportedOperationException("logout() should not be called in portlet mode.");
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		
		// note: the session beans will be kept even when invalidating 
		// the session so they have to be reset (by the exception controller).
		// We invalidate the session however for the other attributes.
		request.getSession().invalidate();
		request.getSession(true);
		// calling this method will reset all the beans of the application
		exceptionController.restart();
		facesContext.responseComplete();
	}
	
	/**
	 * Espace entreprise seulement
	 * @return true if the current user is a Contact
	 */
	public boolean isPageAuthorized() {
		boolean isAuthorized=false;
		if(this.currentAuthContact!=null && this.currentManageStructure!=null) isAuthorized=true;
		return isAuthorized;
	}
	
	/**
	 * Espace entreprise seulement
	 * @return true if the current user is an Admin
	 */
	public boolean isAdminPageAuthorized(){
		boolean isAuthorized=false;
		if(this.currentAuthAdminStructure!=null) isAuthorized=true;
		return isAuthorized;
	}
	
	/**
	 * @return true if the current user is a Super Admin
	 * Entreprise / Stage confondu
	 */
	public boolean isSuperAdminPageAuthorized() {
		boolean isAuthorized=false;
		if((this.currentAuthAdminStructure != null && org.springframework.util.StringUtils.hasText(this.currentAuthAdminStructure.getEppn()))){
			//variable eppn == login
			for(String admin : superAdminTab){
				if(admin.equals(this.currentAuthAdminStructure.getEppn())){
					isAuthorized=true;
				}
			}
		}else if(getCurrentUser() !=null && org.springframework.util.StringUtils.hasText(getCurrentUser().getId())){
			for(String admin : superAdminTab){
				if(admin.equals(getCurrentUser().getId())){
					isAuthorized=true;
				}
			}
		}
		return isAuthorized;
	}


	/* ***************************************************************
	 * Getters / Setters
	 ****************************************************************/

	/**
	 * @param authenticator the authenticator to set
	 */
	public void setAuthenticator(final Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	/**
	 * @param exceptionController the exceptionController to set
	 */
	public void setExceptionController(final ExceptionController exceptionController) {
		this.exceptionController = exceptionController;
	}

	/**
	 * @param printLoginLogoutButtons the printLoginLogoutButtons to set
	 */
	public void setPrintLoginLogoutButtons(final Boolean printLoginLogoutButtons) {
		this.printLoginLogoutButtons = printLoginLogoutButtons;
	}

	/**
	 * @return the casLogoutUrl
	 */
	protected String getCasLogoutUrl() {
		return casLogoutUrl;
	}

	/**
	 * @param casLogoutUrl the casLogoutUrl to set
	 */
	public void setCasLogoutUrl(final String casLogoutUrl) {
		this.casLogoutUrl = StringUtils.nullIfEmpty(casLogoutUrl);
	}

	/**
	 * @return the nousContacter
	 */
	public NousContacter getNousContacter() {
		return nousContacter;
	}

	/**
	 * @param nousContacter the nousContacter to set
	 */
	public void setNousContacter(NousContacter nousContacter) {
		this.nousContacter = nousContacter;
	}

	/**
	 * @return the applicationNameEntreprise
	 */
	public String getApplicationNameEntreprise() {
		return applicationNameEntreprise;
	}

	/**
	 * @param applicationNameEntreprise the applicationNameEntreprise to set
	 */
	public void setApplicationNameEntreprise(String applicationNameEntreprise) {
		this.applicationNameEntreprise = applicationNameEntreprise;
	}

	/**
	 * @return the entrepriseUrl
	 */
	public String getEntrepriseUrl() {
		return entrepriseUrl;
	}

	/**
	 * @param entrepriseUrl the entrepriseUrl to set
	 */
	public void setEntrepriseUrl(String entrepriseUrl) {
		this.entrepriseUrl = entrepriseUrl;
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * @return the applicationNamePStage
	 */
	public String getApplicationNamePStage() {
		return applicationNamePStage;
	}

	/**
	 * @param applicationNamePStage the applicationNamePStage to set
	 */
	public void setApplicationNamePStage(String applicationNamePStage) {
		this.applicationNamePStage = applicationNamePStage;
	}

	/**
	 * @return the mailingListPStage
	 */
	public String getMailingListPStage() {
		return mailingListPStage;
	}

	/**
	 * @param mailingListPStage the mailingListPStage to set
	 */
	public void setMailingListPStage(String mailingListPStage) {
		this.mailingListPStage = mailingListPStage;
	}

	/**
	 * @return the mailingListEntr
	 */
	public String getMailingListEntr() {
		return mailingListEntr;
	}

	/**
	 * @param mailingListEntr the mailingListEntr to set
	 */
	public void setMailingListEntr(String mailingListEntr) {
		this.mailingListEntr = mailingListEntr;
	}

	/**
	 * @return the mailingListEntrMailAvertissementAjoutEtab
	 */
	public boolean isMailingListEntrMailAvertissementAjoutEtab() {
		return mailingListEntrMailAvertissementAjoutEtab;
	}

	/**
	 * @param mailingListEntrMailAvertissementAjoutEtab the mailingListEntrMailAvertissementAjoutEtab to set
	 */
	public void setMailingListEntrMailAvertissementAjoutEtab(
			boolean mailingListEntrMailAvertissementAjoutEtab) {
		this.mailingListEntrMailAvertissementAjoutEtab = mailingListEntrMailAvertissementAjoutEtab;
	}

	/**
	 * @return the mailingListEntrMailAvertissementModifEtab
	 */
	public boolean isMailingListEntrMailAvertissementModifEtab() {
		return mailingListEntrMailAvertissementModifEtab;
	}

	/**
	 * @param mailingListEntrMailAvertissementModifEtab the mailingListEntrMailAvertissementModifEtab to set
	 */
	public void setMailingListEntrMailAvertissementModifEtab(
			boolean mailingListEntrMailAvertissementModifEtab) {
		this.mailingListEntrMailAvertissementModifEtab = mailingListEntrMailAvertissementModifEtab;
	}

	/**
	 * @return the mailingListEntrMailAvertissementSupprEtab
	 */
	public boolean isMailingListEntrMailAvertissementSupprEtab() {
		return mailingListEntrMailAvertissementSupprEtab;
	}

	/**
	 * @param mailingListEntrMailAvertissementSupprEtab the mailingListEntrMailAvertissementSupprEtab to set
	 */
	public void setMailingListEntrMailAvertissementSupprEtab(
			boolean mailingListEntrMailAvertissementSupprEtab) {
		this.mailingListEntrMailAvertissementSupprEtab = mailingListEntrMailAvertissementSupprEtab;
	}

	/**
	 * @return the mailingListEntrMailAvertissementAjoutOffre
	 */
	public boolean isMailingListEntrMailAvertissementAjoutOffre() {
		return mailingListEntrMailAvertissementAjoutOffre;
	}

	/**
	 * @param mailingListEntrMailAvertissementAjoutOffre the mailingListEntrMailAvertissementAjoutOffre to set
	 */
	public void setMailingListEntrMailAvertissementAjoutOffre(
			boolean mailingListEntrMailAvertissementAjoutOffre) {
		this.mailingListEntrMailAvertissementAjoutOffre = mailingListEntrMailAvertissementAjoutOffre;
	}

	/**
	 * @return the mailingListEntrMailAvertissementModifOffre
	 */
	public boolean isMailingListEntrMailAvertissementModifOffre() {
		return mailingListEntrMailAvertissementModifOffre;
	}

	/**
	 * @param mailingListEntrMailAvertissementModifOffre the mailingListEntrMailAvertissementModifOffre to set
	 */
	public void setMailingListEntrMailAvertissementModifOffre(
			boolean mailingListEntrMailAvertissementModifOffre) {
		this.mailingListEntrMailAvertissementModifOffre = mailingListEntrMailAvertissementModifOffre;
	}

	/**
	 * @return the mailingListEntrMailAvertissementSupprOffre
	 */
	public boolean isMailingListEntrMailAvertissementSupprOffre() {
		return mailingListEntrMailAvertissementSupprOffre;
	}

	/**
	 * @param mailingListEntrMailAvertissementSupprOffre the mailingListEntrMailAvertissementSupprOffre to set
	 */
	public void setMailingListEntrMailAvertissementSupprOffre(
			boolean mailingListEntrMailAvertissementSupprOffre) {
		this.mailingListEntrMailAvertissementSupprOffre = mailingListEntrMailAvertissementSupprOffre;
	}

	/**
	 * @return the mailingListPStageIA
	 */
	public InternetAddress getMailingListPStageIA() {
		return mailingListPStageIA;
	}

	/**
	 * @return the mailingListEntrIA
	 */
	public InternetAddress getMailingListEntrIA() {
		return mailingListEntrIA;
	}

	/**
	 * @return the adminAuthentication
	 */
	public String getAdminAuthentication() {
		return adminAuthentication;
	}

	/**
	 * @param adminAuthentication the adminAuthentication to set
	 */
	public void setAdminAuthentication(String adminAuthentication) {
		this.adminAuthentication = adminAuthentication;
	}

	/**
	 * @return the superAdmin
	 */
	public String getSuperAdmin() {
		return superAdmin;
	}

	/**
	 * @param superAdmin the superAdmin to set
	 */
	public void setSuperAdmin(String superAdmin) {
		this.superAdmin = superAdmin;
	}

	/**
	 * @return the superAdminTab
	 */
	public String[] getSuperAdminTab() {
		return superAdminTab;
	}

	/**
	 * @param superAdminTab the superAdminTab to set
	 */
	public void setSuperAdminTab(String[] superAdminTab) {
		this.superAdminTab = superAdminTab;
	}

	/**
	 * @return the currentAuthAdminStructure
	 */
	public AdminStructureDTO getCurrentAuthAdminStructure() {
		return currentAuthAdminStructure;
	}

	/**
	 * @param currentAuthAdminStructure the currentAuthAdminStructure to set
	 */
	public void setCurrentAuthAdminStructure(
			AdminStructureDTO currentAuthAdminStructure) {
		this.currentAuthAdminStructure = currentAuthAdminStructure;
	}

	/**
	 * @return the currentAuthContact
	 */
	public ContactDTO getCurrentAuthContact() {
		return currentAuthContact;
	}

	/**
	 * @param currentAuthContact the currentAuthContact to set
	 */
	public void setCurrentAuthContact(ContactDTO currentAuthContact) {
		this.currentAuthContact = currentAuthContact;
	}

	/**
	 * @return the currentManageStructure
	 */
	public StructureDTO getCurrentManageStructure() {
		return currentManageStructure;
	}

	/**
	 * @param currentManageStructure the currentManageStructure to set
	 */
	public void setCurrentManageStructure(StructureDTO currentManageStructure) {
		this.currentManageStructure = currentManageStructure;
	}

	/**
	 * @return the menuGestionEtab
	 */
	public boolean isMenuGestionEtab() {
		return menuGestionEtab;
	}

	/**
	 * @param menuGestionEtab the menuGestionEtab to set
	 */
	public void setMenuGestionEtab(boolean menuGestionEtab) {
		this.menuGestionEtab = menuGestionEtab;
	}

	/**
	 * @return the currentAuthPersonnel
	 */
	public PersonnelCentreGestionDTO getCurrentAuthPersonnel() {
		return currentAuthPersonnel;
	}

	/**
	 * @param currentAuthPersonnel the currentAuthPersonnel to set
	 */
	public void setCurrentAuthPersonnel(
			PersonnelCentreGestionDTO currentAuthPersonnel) {
		this.currentAuthPersonnel = currentAuthPersonnel;
	}
	/**
	 * @return the currentAuthEnseignant
	 */
	public EnseignantDTO getCurrentAuthEnseignant() {
		return currentAuthEnseignant;
	}
	/**
	 * @param currentAuthEnseignant the currentAuthEnseignant to set
	 */
	public void setCurrentAuthEnseignant(EnseignantDTO currentAuthEnseignant) {
		this.currentAuthEnseignant = currentAuthEnseignant;
	}
	/**
	 * @return the currentAuthEtudiant
	 */
	public EtudiantDTO getCurrentAuthEtudiant() {
		return currentAuthEtudiant;
	}
	/**
	 * @param currentAuthEtudiant the currentAuthEtudiant to set
	 */
	public void setCurrentAuthEtudiant(EtudiantDTO currentAuthEtudiant) {
		this.currentAuthEtudiant = currentAuthEtudiant;
	}
	/**
	 * @return the currentCentresGestion
	 */
	public List<CentreGestionDTO> getCurrentCentresGestion() {
		return currentCentresGestion;
	}
	
	/**
	 * @return List<Integer>
	 */
	public List<Integer> getCurrentIdsCentresGestion(){
		List<Integer> lI = null;
		if(this.currentCentresGestion!=null && !this.currentCentresGestion.isEmpty()){
			lI = new ArrayList<Integer>();
			for(CentreGestionDTO c : this.currentCentresGestion){
				if(c!=null){
					lI.add(c.getIdCentreGestion());
				}
			}
		}
		return lI;
	}

	/**
	 * @param currentCentresGestion the currentCentresGestion to set
	 */
	public void setCurrentCentresGestion(
			List<CentreGestionDTO> currentCentresGestion) {
		this.currentCentresGestion = currentCentresGestion;
	}

	/**
	 * @return the currentStageCasUser
	 */
	public User getCurrentStageCasUser() {
		return currentStageCasUser;
	}
	/**
	 * @param currentStageCasUser the currentStageCasUser to set
	 */
	public void setCurrentStageCasUser(User currentStageCasUser) {
		this.currentStageCasUser = currentStageCasUser;
	}
	/**
	 * @return le login de la personne actuellement connectée (contact, admin entreprise ou personnel pstage)
	 */
	public String getCurrentLogin() {
		if(this.currentAuthAdminStructure!=null){
			this.currentLogin=this.currentAuthAdminStructure.getLogin();
		}else if(this.currentAuthContact!=null){
			this.currentLogin=this.currentAuthContact.getLogin();
		}else if(this.currentAuthEnseignant!=null){
			this.currentLogin=this.currentAuthEnseignant.getUidEnseignant();
		}else if(this.currentAuthEtudiant!=null){
			this.currentLogin=this.currentAuthEtudiant.getIdentEtudiant();
		}else if(this.currentAuthPersonnel!=null){
			this.currentLogin=this.currentAuthPersonnel.getUidPersonnel();
		}else if(this.currentStageCasUser!=null){
			this.currentLogin=this.currentStageCasUser.getId();
		}
		return currentLogin;
	}
		/**
	 * @return the notAdminEntrepriseViaCasShibb
	 */
	public boolean isNotAdminEntrepriseViaCasShibb() {
		return notAdminEntrepriseViaCasShibb;
	}

	/**
	 * @param notAdminEntrepriseViaCasShibb the notAdminEntrepriseViaCasShibb to set
	 */
	public void setNotAdminEntrepriseViaCasShibb(
			boolean notAdminEntrepriseViaCasShibb) {
		this.notAdminEntrepriseViaCasShibb = notAdminEntrepriseViaCasShibb;
	}

	/**
	 * @return the codeUniversite
	 */
	public String getCodeUniversite() {
		return codeUniversite;
	}

	/**
	 * @param codeUniversite the codeUniversite to set
	 */
	public void setCodeUniversite(String codeUniversite) {
		this.codeUniversite = codeUniversite;
	}

	/**
	 * @return the imageUploadBean
	 */
	public ImageUploadBean getImageUploadBean() {
		return imageUploadBean;
	}

	/**
	 * @param imageUploadBean the imageUploadBean to set
	 */
	public void setImageUploadBean(ImageUploadBean imageUploadBean) {
		this.imageUploadBean = imageUploadBean;
	}

	/**
	 * @return the offreFileUploadBean
	 */
	public FileUploadBean getOffreFileUploadBean() {
		return offreFileUploadBean;
	}

	/**
	 * @param offreFileUploadBean the offreFileUploadBean to set
	 */
	public void setOffreFileUploadBean(FileUploadBean offreFileUploadBean) {
		this.offreFileUploadBean = offreFileUploadBean;
	}

	/**
	 * @return the uploadFilesFileSizeLimit
	 */
	public String getUploadFilesFileSizeLimit() {
		return uploadFilesFileSizeLimit;
	}

	/**
	 * @param uploadFilesFileSizeLimit the uploadFilesFileSizeLimit to set
	 */
	public void setUploadFilesFileSizeLimit(String uploadFilesFileSizeLimit) {
		this.uploadFilesFileSizeLimit = uploadFilesFileSizeLimit;
	}

	/**
	 * @return the uploadFilesOffresFileExtensions
	 */
	public String getUploadFilesOffresFileExtensions() {
		return uploadFilesOffresFileExtensions;
	}

	/**
	 * @param uploadFilesOffresFileExtensions the uploadFilesOffresFileExtensions to set
	 */
	public void setUploadFilesOffresFileExtensions(
			String uploadFilesOffresFileExtensions) {
		this.uploadFilesOffresFileExtensions = uploadFilesOffresFileExtensions;
	}

	/**
	 * @return the uploadFilesLogosEntreprisePath
	 */
	public String getUploadFilesLogosEntreprisePath() {
		return uploadFilesLogosEntreprisePath;
	}

	/**
	 * @param uploadFilesLogosEntreprisePath the uploadFilesLogosEntreprisePath to set
	 */
	public void setUploadFilesLogosEntreprisePath(
			String uploadFilesLogosEntreprisePath) {
		this.uploadFilesLogosEntreprisePath = uploadFilesLogosEntreprisePath;
	}

	/**
	 * @return the uploadFilesOffresPath
	 */
	public String getUploadFilesOffresPath() {
		return uploadFilesOffresPath;
	}

	/**
	 * @param uploadFilesOffresPath the uploadFilesOffresPath to set
	 */
	public void setUploadFilesOffresPath(String uploadFilesOffresPath) {
		this.uploadFilesOffresPath = uploadFilesOffresPath;
	}

	/**
	 * @return the uploadFilesLogosCentrePath
	 */
	public String getUploadFilesLogosCentrePath() {
		return uploadFilesLogosCentrePath;
	}

	/**
	 * @param uploadFilesLogosCentrePath the uploadFilesLogosCentrePath to set
	 */
	public void setUploadFilesLogosCentrePath(String uploadFilesLogosCentrePath) {
		this.uploadFilesLogosCentrePath = uploadFilesLogosCentrePath;
	}

	/**
	 * @return the recupererCommunesDepuisApogee
	 */
	public boolean isRecupererCommunesDepuisApogee() {
		return recupererCommunesDepuisApogee;
	}

	/**
	 * @param recupererCommunesDepuisApogee the recupererCommunesDepuisApogee to set
	 */
	public void setRecupererCommunesDepuisApogee(
			boolean recupererCommunesDepuisApogee) {
		this.recupererCommunesDepuisApogee = recupererCommunesDepuisApogee;
	}

	/**
	 * @return the autoriserConventionsOrphelines
	 */
	public boolean isAutoriserConventionsOrphelines() {
		return autoriserConventionsOrphelines;
	}

	/**
	 * @param autoriserConventionsOrphelines the autoriserConventionsOrphelines to set
	 */
	public void setAutoriserConventionsOrphelines(
			boolean autoriserConventionsOrphelines) {
		this.autoriserConventionsOrphelines = autoriserConventionsOrphelines;
	}

	/**
	 * @return the autoriserCentresBloquerImpressionConvention
	 */
	public boolean isAutoriserCentresBloquerImpressionConvention() {
		return autoriserCentresBloquerImpressionConvention;
	}

	/**
	 * @param autoriserCentresBloquerImpressionConvention the autoriserCentresBloquerImpressionConvention to set
	 */
	public void setAutoriserCentresBloquerImpressionConvention(
			boolean autoriserCentresBloquerImpressionConvention) {
		this.autoriserCentresBloquerImpressionConvention = autoriserCentresBloquerImpressionConvention;
	}

	/**
	 * @return the autoriserEntrepriseAReserverOffre
	 */
	public boolean isAutoriserEntrepriseAReserverOffre() {
		return autoriserEntrepriseAReserverOffre;
	}

	/**
	 * @param autoriserEntrepriseAReserverOffre the autoriserEntrepriseAReserverOffre to set
	 */
	public void setAutoriserEntrepriseAReserverOffre(
			boolean autoriserEntrepriseAReserverOffre) {
		this.autoriserEntrepriseAReserverOffre = autoriserEntrepriseAReserverOffre;
	}

	/**
	 * @return the autoriserEtudiantAModifierEntreprise
	 */
	public boolean isAutoriserEtudiantAModifierEntreprise() {
		return autoriserEtudiantAModifierEntreprise;
	}

	/**
	 * @param autoriserEtudiantAModifierEntreprise the autoriserEtudiantAModifierEntreprise to set
	 */
	public void setAutoriserEtudiantAModifierEntreprise(
			boolean autoriserEtudiantAModifierEntreprise) {
		this.autoriserEtudiantAModifierEntreprise = autoriserEtudiantAModifierEntreprise;
	}

	/**
	 * @return the centreGestionRattachement
	 */
	public CentreGestionDTO getCentreGestionRattachement() {
		return centreGestionRattachement;
	}

	/**
	 * @param centreGestionRattachement the centreGestionRattachement to set
	 */
	public void setCentreGestionRattachement(
			CentreGestionDTO centreGestionRattachement) {
		this.centreGestionRattachement = centreGestionRattachement;
	}

	/**
	 * @return the isEnt
	 */
	public Boolean getIsEnt() {
		return isEnt;
	}

	/**
	 * @param isEnt the isEnt to set
	 */
	public void setIsEnt(Boolean isEnt) {
		this.isEnt = isEnt;
	}

	/**
	 * @param uploadImagesFileSizeLimit the uploadImagesFileSizeLimit to set
	 */
	public void setUploadImagesFileSizeLimit(String uploadImagesFileSizeLimit) {
		this.uploadImagesFileSizeLimit = uploadImagesFileSizeLimit;
	}

	/**
	 * @return the uploadImagesFileSizeLimit
	 */
	public String getUploadImagesFileSizeLimit() {
		return uploadImagesFileSizeLimit;
	}

	/**
	 * @param uploadImagesFileExtensions the uploadImagesFileExtensions to set
	 */
	public void setUploadImagesFileExtensions(String uploadImagesFileExtensions) {
		this.uploadImagesFileExtensions = uploadImagesFileExtensions;
	}

	/**
	 * @return the uploadImagesFileExtensions
	 */
	public String getUploadImagesFileExtensions() {
		return uploadImagesFileExtensions;
	}

	/**
	 * @param critereGestion the critereGestion to set
	 */
	public void setCritereGestion(String critereGestion) {
		this.critereGestion = critereGestion;
	}

	/**
	 * @return the critereGestion
	 */
	public String getCritereGestion() {
		return critereGestion;
	}

	/**
	 * @return the cvtheque
	 */
	public boolean isCvtheque() {
		return cvtheque;
	}

	/**
	 * @param cvtheque the cvtheque to set
	 */
	public void setCvtheque(boolean cvtheque) {
		this.cvtheque = cvtheque;
	}

	/**
	 * @return the cvthequeUrl
	 */
	public String getCvthequeUrl() {
		return cvthequeUrl;
	}

	/**
	 * @param cvthequeUrl the cvthequeUrl to set
	 */
	public void setCvthequeUrl(String cvthequeUrl) {
		this.cvthequeUrl = cvthequeUrl;
	}
		

	/**
	 * @return the assistance
	 */
	public boolean isAssistance() {
		return assistance;
	}

	/**
	 * @param assistance the assistance to set
	 */
	public void setAssistance(boolean assistance) {
		this.assistance = assistance;
	}

	/**
	 * @return the urlAssistance
	 */
	public String getUrlAssistance() {
		return urlAssistance;
	}

	/**
	 * @param urlAssistance the urlAssistance to set
	 */
	public void setUrlAssistance(String urlAssistance) {
		this.urlAssistance = urlAssistance;
	}

	/**
	 * @param droitsAccesMap the droitsAccesMap to set
	 */
	public void setDroitsAccesMap(Map<Integer, DroitAdministrationDTO> droitsAccesMap) {
		this.droitsAccesMap = droitsAccesMap;
	}

	/**
	 * @return the droitsAccesMap
	 */
	public Map<Integer, DroitAdministrationDTO> getDroitsAccesMap() {
		return droitsAccesMap;
	}

	/**
	 * @return the enseignantTuteur
	 */
	public boolean isEnseignantTuteur() {
		return enseignantTuteur;
	}

	/**
	 * @param enseignantTuteur the enseignantTuteur to set
	 */
	public void setEnseignantTuteur(boolean enseignantTuteur) {
		this.enseignantTuteur = enseignantTuteur;
	}

	/**
	 * @param moderationActive the moderationActive to set
	 */
	public void setModerationActive(boolean moderationActive) {
		this.moderationActive = moderationActive;
	}

	/**
	 * @return the moderationActive
	 */
	public boolean isModerationActive() {
		return moderationActive;
	}
	
	
}