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
            const newFav = new Favourites(req.body)
            const saveFav = await newFav.save()
            res.status(200).json(saveFav)
        }
    } catch (error) {
        console.error('Error:', error)
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
})

router.get("/yeuthich/:id", async function (req, res) {
    try {
        const favourites = await yeuThichs.find({ nguoi_dung_id: req.params.nguoi_dung_id });
        res.status(200).json(favourites);
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
});
module.exports = router;
