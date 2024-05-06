# Role Based Access Control System with Spring (Basic Configuration for any System) Working with authentication and authorization to control everything on the system

- For Roles

-Admin, User


- For Privileges (Permissions) ( * in the Spring you have to you privilege or permission instead of authority becasue the default name convention inside framework is authority so you have to use what recommend at first to avoid the conflict)

-Create, Read, Update, Delete

# ##############
Besides that, using Multi-Factor Authentication for system access


Using Open Documentation API for Swagger (SpringDoc and open api 3)


Also will add all the packages and dependencies that will be used with this structure
# ######################
for jwt dependecy in this branch will use:

- jjwt version(
  implementation group: 'io.jsonwebtoken', name: 'jjwt-api', version: '0.12.5'
	runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-impl', version: '0.12.5'
	runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-jackson', version: '0.12.5'
 )
  
**  you can find them at https://www.mvnrepository.com
