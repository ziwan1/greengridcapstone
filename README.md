# New Renewable Energy Placement Recommendation System for Solar Panels Based on the Location of East Java District Using Geospatial Analysis, Statistical Approaches, and Deep Learning

# ML README

Our team collected the dataset from January 1 2010 to April 10 2023. The dataset consists of sub-district weather geographic data in the form of a collection of temperature, humidity, air pressure, rainfall, cloud cover, solar radiation, direct solar irradiation level, and wind speed. We got this dataset from the open meteo website. This dataset records the hourly changes of the above data in the period of January 1 2010 to April 10, 2023. We're able to get the dataset by scraping the openmeteo API using the script contained in the apiscraper.ipynb file.

Link to the dataset: https://intip.in/datasetcuaca

Use the linecutter.py script to delete the first 3 lines of each csv in the dataset.

In this GitHub directory there will be several models of training results from the datasets we collected. 

Other regions can be trained with the following steps:
1. Extract the capstone.zip file
2. Add the CSV contained in the dataset / add your own dataset
3. Change the destination in the ts_forecasting.py file to match your directory
4. Use the conda env create -f environment.yml command according to the .yml file found on this Github (recommended renaming the environment)
5. Activate the imported environment with the command conda activate <environment_name>
6. Open a command prompt and cd to the unzipped directory
7. Run the Python command
8. Wait for the training to finish

This model was developed to forecast Indonesian cities short and long-term weather using LSTM-based deep learning. The dataset was collected and preprocessed, including handling time data and applying feature engineering techniques. The model architecture consists of an input layer, an LSTM layer, and fully connected layers. The model was trained using Mean Squared Error loss and Adam optimizer. Geospatial and statistical features were incorporated into the dataset. Model performance was evaluated using Root Mean Squared Error and R-squared metrics. The model's forecasted results were visualized and saved. The entire process was executed using TensorFlow and various supporting libraries. Adjust the hours for the forecasting results in def construct_lt_prediction and forecast_df['time'] = palette.reset_index()['time'][0:1000]

# CC README
Our team use Express.js as Node.js framework to build an API for register, login, and predict. For register and login, we use MySQL database from CloudSQL as user data storage. For predict, we first put ML model outputs in .png format to cloud storage, then the API provides image urls from the cloud storage according to the district and sub-district input from the user.

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

# MD README
So here is the mobile application, our team made this application as a viewer for various data related to weather data and so on. For the weather data api, our team uses the api from open-meteo.com, where the data taken is in the form of temperature, humidity, precipitation, direct normal irradiance, and cloud cover data. In addition, this application takes pictures of the results of training data by the ML team, the data api of which is made by the CC team. This application displays graphic images of related training data items such as temperature, humidity, etc. from a sub-district in a district. In this application, users can search for areas where data and graphics are related to renewable energy sources. If you want to try installing the application, you can directly install app-debug.apk

