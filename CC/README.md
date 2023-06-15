Our team use Express.js as Node.js framework to build an API for register, login, and predict. For register and login, we use MySQL database from CloudSQL as user data storage. For predict, we first put ML model outputs in .png format to cloud storage, then the API provides image urls from the cloud storage according to the district and sub-district input from the user.

We have deployed API to App Engine in Google Cloud Platform, and there are 3 endpoints: <br>
https://bangkit-capstone-388003.et.r.appspot.com/register (register) <br>
https://bangkit-capstone-388003.et.r.appspot.com/login (login) <br>
https://bangkit-capstone-388003.et.r.appspot.com/predict/:kabupaten/:kecamatan (predict)

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
