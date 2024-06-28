const router = require('express').Router()
const yeuThichs = require('../model/yeuThich')
const nguoiDungs = require('../model/nguoiDung')
const noiThats = require('../model/noiThat')
const moment = require('moment');

// hàng mới về
router.post('/new', async function (req, res) {
    try {
        
    } catch (error) {
        console.error('Error:', error)
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
})

router.get("/banchay", async function (req, res) {
    try {
       
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
});


module.exports = router;
