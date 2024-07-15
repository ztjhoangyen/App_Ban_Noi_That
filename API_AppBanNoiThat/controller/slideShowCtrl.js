const router = require('express').Router()
const SlideShow = require('../model/SlideShow')

router.get('/slide', async function (req, res) {
    try {
        const exitsGet = await SlideShow.find()
        res.status(200).json(exitsGet)
    } catch (err) {
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})

module.exports = router