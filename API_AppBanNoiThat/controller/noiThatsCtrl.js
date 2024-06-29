const router = require('express').Router()
const noiThats = require('../model/noiThat')

router.get('/noiThat/:id', async function(req, res) {
    try {
        const exitsGet = await noiThats.find({loai_noi_that_id: req.params.id})
        
        if(exitsGet.length == 0){
            res.status(404).json({error: "Danh sách nội thất trống"})
            return
        }

        res.status(200).json(exitsGet)
    } catch (err) {
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." });
    }
});

router.get('/noiThatCT/:id', async function(req, res) {
    try {
        const exitsGetCT = await noiThats.findById(req.params.id)
        
        if(!exitsGetCT){
            res.status(404).json({error: "Nội thất null"})
        }

        res.status(200).json({error: "Chi tiết nội thất tồn tại" + exitsGetCT})
    } catch (err) {
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." });
    }
});

// http://localhost:3000/api/timKiem?query=
router.get("/timKiem", async function(req, res, next) {
    try {
        const query = req.query.query;
        // $options: "i": options là tùy chọn, i là không phân biệt hoa, thường
        const timKiemNoiThat = await noiThats.find({ten_noi_that : { $regex: query, $options: "i" } });

        res.status(200).json(timKiemNoiThat); 
        console.log(timKiemNoiThat);
    } catch (e) {
        res.status(500).json({ error: "Lỗi máy chủ cục bộ" });
    }
})


module.exports = router