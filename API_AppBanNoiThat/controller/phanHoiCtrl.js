const route = require('express').Router()
const phanHois = require('../model/phanHoi')

route.post('/phanhoi', async function(req, res, next){
    try{

        // const newphanhoi = new phanHois(req.body)
        // await newphanhoi.save()
        const exist = await phanHois.findOne({ nguoi_dung_id: req.body.nguoi_dung_id, danh_gia_id: req.body.danh_gia_id })
       
        if (exist) {
            return res.status(409).json({ error: "Đã phản hồi" })
        }
        const newphanhoi = new phanHois({
            nguoi_dung_id : req.body.nguoi_dung_id,
            danh_gia_id : req.body.danh_gia_id,
            noi_dung : req.body.noi_dung
        })
        await newphanhoi.save()

        res.status(200).json({message: "Success"})
    }catch(err){
        res.status(500).json(err)
    }
})

route.get('/phanHois', async function (req, res, next) {
    try {
        const phanhois = await phanHois.find()

        if (phanhois.length == 0) {
            return res.status(404).json({ error:  "Not found" })
        } 

        res.status(200).json(phanhois)
    } catch (err) {
        res.status(500).json(err)
    }
})

module.exports = route