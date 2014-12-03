CentraSite Application Framework - Persistence framework over JAXR
==================================================================
version 8.2.5.0 /17.03.2012


I. Introduction
----------------

	The CentraSite Application Framework (CSAF) provides persistence and validation services for developing custom applications for CentraSite.


II. Installation
-----------------------------

	Requirements
	
		This version of CSAF is compatible with and requires CentraSite version 8.2. 
		The version of JDK to be used should be the same as the one that comes with the installation of CentraSite. 
		
	Installation steps
	
		(1) Unzip into directory of your choice.
		(2) Framework jars are in the redist/csaf folder of the CentraSite installation.
		(3) Latest documentation and javadoc are part of the CentraSite Product documentation.
		(4) Eclipse project files for the sample are located in the demos directory.
   
   
Get Up And Running Quick with Demo
-------------------------------------

	Sample demos are distributed in the demos subdirectory.

	Requirements:
	   (1) Installed CentraSite server

	Running Demo:
	   
	   (1) Edit conf.properties in the demo/CRUD-simple directory by specifying
	    (1.1) Registry username and password to be used when executing the samples
	    (1.2) CentraSite URL.
	    (1.3) Path pointing to the Package-CSAF-SDK.zip.
	    (1.4) Path pointing to the CentraSite redist folder.
	          Default values are present in the file.
	   (2) run "ant" or "ant all" in the demo directory
	   (3) run "ant noDelete" in the demo directory to execute the demo 
	       without removing the created objects from the registry 
	    
	    NOTE that the sample itself takes care that the Package-CSAF-SDK.zip is imported into CentraSite.
	    	There is also a "manual" way to import it using CentraSite UI:
	    	
	    (1) Open CentraSite Control Click on the Asset Catalog Perspective
        (2) Click on the Add Asset(s) button
        (3) Choose "Add new assets using and importer" and select "Archive" from the drop-down list.
        (4) Follow the wizard.   
   
       
Exploring the demo source code
--------------------------------

   The demos come with a predefined Eclipse project, which you could directly import in your workspace.
   The following variables are used by the project and need to be defined in your Eclipse:
   		(1)  CSAF_SDK_HOME - directory where the sample is placed
   		(2)  REDIST - directory where the CentraSite jars are stored. Normally it is C:\SoftwareAG\CentraSite\redist.
   	
   		

   		      