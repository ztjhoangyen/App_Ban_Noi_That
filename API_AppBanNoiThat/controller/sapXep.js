const router = require('express').Router()
const yeuThichs = require('../model/yeuThich')
const nguoiDungs = require('../model/nguoiDung')
const noiThats = require('../model/noiThat')

const { subDays, startOfDay } = require('date-fns');
// subDays: Hàm này trừ đi số ngày từ một ngày cụ thể.
// startOfDay: Hàm này thiết lập thời gian của một ngày cụ thể về 0:00 giờ

router.get("/sanPhamMoi/:id", async function (req, res) {
    try {
        const currentDate = startOfDay(new Date()); // Ngày hiện tại, tính từ đầu ngày
        const thirtyDaysAgo = startOfDay(subDays(new Date(), 30)); // Ngày cách đây 30 ngày

        const sanPhamMoi = await noiThats.find({
            loai_noi_that_id: req.params.id,
            //  lớn hơn hoặc bằng thirtyDaysAgo và nhỏ hơn currentDate.
            ngay_nhap_kho: { $gte: thirtyDaysAgo, $lt: currentDate }
        });

        res.status(200).json(sanPhamMoi);
    } catch (e) {
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." });
    }
});


// router.get("/banchay", async function (req, res) {
//     try {

//     } catch (err) {
//         res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
//     }
// });

router.get("/tang/:id", async function (req, res) {
    try {
        const listTang = await noiThats.find({loai_noi_that_id: req.params.id})
        if(listTang.length == 0){
            res.status(404).json({error: "Danh sách nội thất trống"})
            return
        }
        await listTang.sort((a, b) => a.gia - b.gia)
        res.status(200).json(listTang)
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
});

router.get("/giam/:id", async function (req, res) {
    try {
        const listGiam = await noiThats.find({loai_noi_that_id: req.params.id})
        if(listGiam.length == 0){
            res.status(404).json({error: "Danh sách nội thất trống"})
            return
        }
        await listGiam.sort((a, b) => b.gia - a.gia)
        res.status(200).json(listGiam)
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
});

router.get("/banchay", async function (req, res) {
    try {

    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
});

module.exports = router;
