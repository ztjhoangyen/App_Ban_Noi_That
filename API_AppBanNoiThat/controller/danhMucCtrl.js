const router = require('express').Router()
const danhMucs = require('../model/danhMuc')

router.get('/danhMuc', async function(req, res) {
    try {
        const exitsGet = await danhMucs.find()
        
        if(exitsGet.length == 0){
            res.status(404).json({error: "Danh mục trống"})
            return;
        }

        res.status(200).json(exitsGet)
    } catch (err) {
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." });
    }
});

module.exports = router