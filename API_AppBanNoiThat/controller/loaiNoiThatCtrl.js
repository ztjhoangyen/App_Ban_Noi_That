const router = require('express').Router()
const loaiNoiThats = require('../model/loaiNoiThat')

router.get('/loaiNoiThat', async function(req, res) {
    try {
        const exitsGet = await loaiNoiThats.find()
        
        if(exitsGet.length == 0){
            res.status(404).json({error: "Danh sách loại nội thất trống"})
        }

        res.status(200).json({error: "Tồn tại danh sách" + exitsGet})
    } catch (err) {
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." });
    }
});

module.exports = router