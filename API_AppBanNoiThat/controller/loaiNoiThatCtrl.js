const router = require('express').Router()
const loaiNoiThats = require('../model/loaiNoiThat')

router.get('/loaiNoiThat/:id', async function(req, res) {
    try {
        const exitsGet = await loaiNoiThats.find({id_danh_muc: req.params.id})
        
        if(exitsGet.length == 0){
            res.status(404).json({error: "Danh sách loại nội thất trống"})
            return
        }

        res.status(200).json(exitsGet)
    } catch (err) {
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})

router.get('/AllloaiNoiThat', async function(req, res) {
    try {
        const allLoai = await loaiNoiThats.find()
        
        if(allLoai.length == 0){
            res.status(404).json({error: "Danh sách loại nội thất trống"})
            return
        }

        res.status(200).json(allLoai)
    } catch (err) {
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})

module.exports = router