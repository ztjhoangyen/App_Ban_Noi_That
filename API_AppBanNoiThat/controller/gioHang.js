const router = require('express').Router()
const gioHangs = require('../model/gioHang');
const gioHangChiTiets = require('../model/gioHangChiTiet');
const noiThats = require('../model/noiThat');

// Khi đăng nhập vào thì kiểm tra xem có tồn tại giỏ hàng với trạng thái là 1(chưa thanh toán kh)
// router.post('/gioHang', async function(req, res) {
//     try {
//        let gioHang = await gioHangs.findOne({ nguoi_dung_id: req.body.nguoi_dung_id, trang_thai: 1 });

//        if (!gioHang) {
//            gioHang = new gioHangs({ nguoi_dung_id: req.body.nguoi_dung_id });
//            await gioHang.save();
//            res.status(201).json(gioHang);
//        } else {
//            res.status(200).json({message: "Đã tồn tại"});
//        }
//     } catch (error) {
//         console.error('Error:', error);
//         res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' });
//     }
// });

// router.post('/gioHangVaChiTiet', async (req, res) => {
//     const { nguoi_dung_id, noi_that_id, so_luong, gia } = req.body;
// // 1. tạo thành công
// // 2. không có tạo thêm giỏ hàng hay giỏ hàng chi tiết
// // 3 thay đổi idnoiThat tạo thêm GHCT

//     try {
//         // Tìm giỏ hàng của người dùng
//         let gioHang = await gioHangs.findOne({ nguoi_dung_id: nguoi_dung_id, trang_thai: 1 });

//         // Nếu giỏ hàng không tồn tại, tạo mới
//         if (!gioHang) {
//             gioHang = new gioHangs({ nguoi_dung_id: nguoi_dung_id });
//             await gioHang.save();
//         }

//         // Tìm hoặc tạo mới chi tiết giỏ hàng
//         let gioHangChiTiet = await gioHangChiTiets.findOne({ gio_hang_id: gioHang._id, noi_that_id: noi_that_id });

//         if (gioHangChiTiet) {
//             // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng và giá
//             gioHangChiTiet.so_luong += so_luong;
//             gioHangChiTiet.gia = gia;  // Bạn có thể cập nhật giá hoặc giữ nguyên tùy theo logic của bạn
//         } else {
//             // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới chi tiết giỏ hàng
//             gioHangChiTiet = new gioHangChiTiets({
//                 gio_hang_id: gioHang._id,
//                 noi_that_id: noi_that_id,
//                 so_luong: so_luong,
//                 gia: gia
//             });
//         }

//         await gioHangChiTiet.save();

//         // Trả về kết quả giỏ hàng cùng chi tiết giỏ hàng
//         res.status(201).json({
//             gioHang: gioHang,
//             gioHangChiTiet: gioHangChiTiet
//         });
//     } catch (error) {
//         console.error('Error:', error);
//         res.status(500).json({ error: 'Lỗi máy chủ nội bộ.' });
//     }
// });

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

router.delete('/delGHCT/:id', async (req, res) => {
    try{
        const delGH = gioHangChiTiets.deleteOne({gio_hang_id: req.params.id})
        res.status(200).json(delGH)
    }catch(err){
        res.status(500).json(delGH)
    }
})

module.exports = router