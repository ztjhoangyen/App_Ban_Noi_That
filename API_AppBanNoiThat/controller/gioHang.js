const router = require('express').Router()
const gioHangs = require('../model/gioHang')

// Khi đăng nhập vào thì kiểm tra xem có tồn tại giỏ hàng với trạng thái là 1(chưa thanh toán kh)
router.post('/gioHang', async function(req, res) {
    try {
       let gioHang = await gioHangs.findOne({ nguoi_dung_id: req.body.nguoi_dung_id, trang_thai: 1 });

       if (!gioHang) {
           gioHang = new gioHangs({ nguoi_dung_id: req.body.nguoi_dung_id });
           await gioHang.save();
           res.status(201).json(gioHang);
       } else {
           res.status(200).json({message: "Đã tồn tại"});
       }
    } catch (error) {
        console.error('Error:', error);
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' });
    }
});

module.exports = router