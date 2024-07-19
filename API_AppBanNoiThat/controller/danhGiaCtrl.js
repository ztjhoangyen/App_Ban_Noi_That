const route = require('express').Router()
const danhGias = require('../model/danhGia')

route.post('/danhgia', async function (req, res, next) {
    try {
        const exist = await danhGias.findOne({ nguoi_dung_id: req.body.nguoi_dung_id, hoa_don_id: req.body.hoa_don_id })
       
        if (exist) {
            return res.status(409).json({ error: "Đã đánh giá" })
        }
        const newdanhGia = new danhGias({
            nguoi_dung_id : req.body.nguoi_dung_id,
            hoa_don_id : req.body.hoa_don_id,
            diem : req.body.diem,
            binh_luan : req.body.binh_luan
        })
        await newdanhGia.save();
        res.status(200).json({mess: "Success"})
    } catch (error) {
        res.status(500).json(error)
    }
})

// all đanhs giá
route.get('/danhGias', async function (req, res, next) {
    try {
        const danhgias = await danhGias.find()

        if (danhgias.length == 0) {
            return res.status(404).json({ error:  "Not found" })
        } 

        res.status(200).json(danhgias)
    } catch (err) {
        res.status(500).json(err)
    }
})

module.exports = route