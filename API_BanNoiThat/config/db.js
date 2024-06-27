const {response} = require('express')
const mongoose = require('mongoose')
const dotenv = require('dotenv')

dotenv.config()

mongoose.connect("mongodb://localhost:27017/AppBanNoiThat", {
    useNewUrlParser: true,
    useUnifiedTopology: true
}).then(response =>{
    console.log('MongoDB connection Succeeded');
}).catch(error =>{
    console.log('Erro in DB connect');
})