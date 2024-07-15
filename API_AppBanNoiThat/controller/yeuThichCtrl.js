const router = require('express').Router()
const yeuThichs = require('../model/yeuThich')
const nguoiDungs = require('../model/nguoiDung')
const noiThats = require('../model/noiThat')

router.post('/yeuthich', async function (req, res) {
    try {
        const existFav = await yeuThichs.find({ nguoi_dung_id: req.body.nguoi_dung_id, noi_that_id: req.body.noi_that_id })
        if (existFav.length > 0) {
            await yeuThichs.deleteOne({ nguoi_dung_id: req.body.nguoi_dung_id, noi_that_id: req.body.noi_that_id });
            return res.status(200).json({ message: "Đã xóa" })
        } else {
            const newFav = new yeuThichs(req.body)
            const saveFav = await newFav.save()
            return res.status(200).json({ message: "Đã thêm" })
        }
    } catch (error) {
        console.error('Error:', error)
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
})

router.get("/yeuthich/:id", async function (req, res) {
    try {
        const favourites = await yeuThichs.find({ nguoi_dung_id: req.params.id });
        res.status(200).json(favourites);
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
})

router.get("/totalFav", async function (req, res, next) {
    try {
        const listNoiThat = await noiThats.find();
        if(listNoiThat.length === 0){
            return res.status(404).json({error: "Not listNoiThat"})
        }

        let totals = []
        for(let i = 0; i < listNoiThat.length; i++){
            const listFav = await yeuThichs.find({noi_that_id: listNoiThat[i]._id})

            totals.push({
                ID_noiThat: listNoiThat[i]._id,
                total: listFav.length
            })
        }
        res.status(200).json(totals);
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
})

module.exports = router;
