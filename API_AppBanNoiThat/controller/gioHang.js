const router = require('express').Router()
const gioHangs = require('../model/gioHang');
const gioHangChiTiets = require('../model/gioHangChiTiet');
const noiThats = require('../model/noiThat');

router.post('/gioHangVaChiTiet', async (req, res) => {
    const { nguoi_dung_id, noi_that_id, so_luong, gia } = req.body;

    try {
        let gioHang = await gioHangs.findOne({ nguoi_dung_id: nguoi_dung_id, trang_thai: 1 });

        if (!gioHang) {
            gioHang = new gioHangs({ nguoi_dung_id: nguoi_dung_id });
            await gioHang.save();
        }

        let gioHangChiTiet = await gioHangChiTiets.findOne({ gio_hang_id: gioHang._id, noi_that_id: noi_that_id });

        const sanPham = await noiThats.findById(noi_that_id); 
        if (!sanPham) {
            return res.status(404).json({ error: 'Sản phẩm không tồn tại' });
        }
        const soLuongTonKho = sanPham.so_luong; 

        if (gioHangChiTiet) {
            return res.status(409).json({ error: 'Đã tồn tại, sang trang giỏ hàng' });
        } else {
            if (so_luong > soLuongTonKho) {
                return res.status(400).json({ error: 'Số lượng mua vượt quá số lượng tồn kho' });
            }

            gioHangChiTiet = new gioHangChiTiets({
                gio_hang_id: gioHang._id,
                noi_that_id: noi_that_id,
                so_luong: so_luong,
                gia: gia
            });
        }

        await gioHangChiTiet.save();

        res.status(201).json({
            gioHang: gioHang,
            gioHangChiTiet: gioHangChiTiet
        });
    } catch (error) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' });
    }
});

router.post('/updateChiTietGH', async (req, res) => {
    const { nguoi_dung_id, noi_that_id, so_luong, gia } = req.body;

    try {
        let gioHang = await gioHangs.findOne({ nguoi_dung_id: nguoi_dung_id, trang_thai: 1 });

        if (!gioHang) {
            return res.status(404).json({ error: 'Không tìm thấy giỏ hàng hiện tại' });
        }

        let gioHangChiTiet = await gioHangChiTiets.findOne({ gio_hang_id: gioHang._id, noi_that_id: noi_that_id });

        if (!gioHangChiTiet) {
            return res.status(404).json({ error: 'Chi tiết giỏ hàng không tồn tại' });
        }

        const sanPham = await noiThats.findById(noi_that_id); 
        if (!sanPham) {
            return res.status(404).json({ error: 'Sản phẩm không tồn tại' });
        }
        const soLuongTonKho = sanPham.so_luong;

        const newQuantity = so_luong;

        if (newQuantity > soLuongTonKho) {
            return res.status(400).json({ error: 'Số lượng mua vượt quá số lượng tồn kho' });
        }

        gioHangChiTiet.so_luong = newQuantity;
        gioHangChiTiet.gia = gia; 

        await gioHangChiTiet.save();

        res.status(200).json({
            gioHang: gioHang,
            gioHangChiTiet: gioHangChiTiet
        });
    } catch (error) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' });
    }
})

router.delete('/delGHCT/:id', async (req, res, next) => {
    try {
        const deletedItem = await gioHangChiTiets.findOneAndDelete(req.params.id);

        if (!deletedItem) {
            console.log("kh xóa được" + deletedItem);
            return res.status(404).json({ error: 'Không tìm thấy chi tiết giỏ hàng với ID đã cho.' });
        }
        res.status(200).json('Xóa thành công.');
    } catch (err) {
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' });
    }
});


module.exports = router