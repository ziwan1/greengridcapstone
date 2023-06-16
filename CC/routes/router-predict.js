const express = require('express');
const router = express.Router();
const path = require('path');

const { Storage } = require('@google-cloud/storage');
const pathKey = path.resolve('./serviceaccountkey.json')

const storage = new Storage({
    projectId: 'bangkit-capstone-388003',
    keyFilename: pathKey, 
  });
const bucketName = 'east-java-forecast-data'; 

// Definisikan endpoint untuk meminta URL gambar berdasarkan kabupaten dan kecamatan
router.get('/:kabupaten/:kecamatan', async (req, res) => {
    const kabupaten = req.params.kabupaten.toLowerCase();
    const kecamatan = req.params.kecamatan.toLowerCase();

    try {
      const prediction = {
        temperature_2m: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/1.png`,
        relativehumidity_2m: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/2.png`,
        apparent_temperature: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/3.png`,
        precipitation: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/4.png`,
        rain: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/5.png`,
        cloudcover: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/6.png`,
        shortwave_radiation: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/7.png`,
        direct_radiation: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/8.png`,
        diffuse_radiation: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/9.png`,
        direct_normal_irradiance: `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/10.png`
      };

      for (let i = 1; i <= 10; i++) {
        const [fileExists] = await storage.bucket(bucketName).file(`${kabupaten}/${kecamatan}/${i}.png`).exists();
        if (!fileExists) {
          throw new Error('File not found');
        }
      }
     
      const response = {
        message: 'success',
        prediction
      };
  
      res.json(response);

    } catch (err) {
      console.error(err);
      res.status(500).send('Terjadi kesalahan saat mengambil URL gambar');
    }
  });
  
module.exports = router;
