API endpoint can be created with the following steps:
1. Clone this repository
2. Run this command: <br>
npm install @google-cloud/storage <br>
npm install body-parser <br>
npm install express <br>
npm install express-session <br>
npm install mysql
3. Create a Cloud SQL Instance and database for your application
4. Note the public IP SQL instance, database name, and SQL password
5. Change the database configuration in database.js use the information noted in the previous steps
6. Create a service account, grant the roles as Storage Admin, and then download the JSON key
7. Copy and paste the JSON key to the serviceaccountkey.json file
8. Deploy to app engine to create an endpoint
