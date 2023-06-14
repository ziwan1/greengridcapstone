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
      const urls = [];
      for (let i = 1; i <= 10; i++) {
        const imageUrl = `https://storage.googleapis.com/${bucketName}/${kabupaten}/${kecamatan}/${i}.png`;
        const [fileExists] = await storage.bucket(bucketName).file(`${kabupaten}/${kecamatan}/${i}.png`).exists();

        if (!fileExists) {
          throw new Error('File not found');
        }
          urls.push(imageUrl);
        }

        res.json(urls);

    } catch (err) {
      console.error(err);
      res.status(500).send('Terjadi kesalahan saat mengambil URL gambar');
    }
  });
  
  module.exports = router;