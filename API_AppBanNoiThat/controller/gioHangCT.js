const router = require('express').Router()
const gioHangChiTiets = require('../model/gioHangChiTiet')
const gioHangs = require('../model/gioHang')
const noiThats = require('../model/noiThat')

// Lấy chi tiết giỏ hàng chưa thanh toán của người dùng
router.get('/gioHangCT/:id', async function (req, res) {
    try {
        const gioHang = await gioHangs.findOne({ nguoi_dung_id: req.params.id, trang_thai: 1 })
        if (!gioHang) {
            return res.status(404).json({ error: 'Không tìm thấy giỏ hàng chưa thanh toán.' })
        }

        const gioHangChiTiet = await gioHangChiTiets.find({ gio_hang_id: gioHang._id })
            .populate('noi_that_id')

        res.status(200).json(gioHangChiTiet)
    } catch (error) {
        console.error('Error:', error)
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' })
    }
})

//  Thêm hoặc cập nhật sản phẩm vào giỏ hàng chi tiết
// CHÚ Ý: tính giá bên UI
router.post('/gioHangCT', async (req, res) => {
    const { gio_hang_id, noi_that_id, so_luong, gia } = req.body;
    try {
        let gioHangChiTiet = await gioHangChiTiets.findOne({ gio_hang_id, noi_that_id });

        if (gioHangChiTiet) {
            console.log('Sản phẩm đã có trong giỏ hàng chi tiết.');
        } else {
            gioHangChiTiet = new gioHangChiTiets({ gio_hang_id, noi_that_id, so_luong, gia});
        }

        await gioHangChiTiet.save();
        res.status(201).json(gioHangChiTiet);
    } catch (error) {
        console.error('Error:', error);
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' });
    }
})

router.put('/gioHangCT/:id', async (req, res) => {
    const { id } = req.params;
    const { so_luong } = req.body;

    try {
        let gioHangChiTiet = await gioHangChiTiets.findById(id);

        if (!gioHangChiTiet) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm trong giỏ hàng chi tiết.' });
        }

        const noiThat = await noiThats.findById(gioHangChiTiet.noi_that_id);
        if (!noiThat) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm.' });
        }

        if (so_luong > noiThat.so_luong) {
            return res.status(400).json({ error: 'Sản phẩm không đủ số lượng.' });
        }

        gioHangChiTiet.so_luong = so_luong;
        await gioHangChiTiet.save();

        res.status(200).json(gioHangChiTiet);
    } catch (error) {
        console.error('Error:', error);
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' });
    }
});

module.exports = router;
