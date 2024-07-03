const donHang = require('../model/donHang')
const gioHang = require('../model/gioHang')
const gioHangChiTiet = require('../model/gioHangChiTiet')
const noiThat = require('../model/noiThat')
const donHangChiTiets = require('../model/donHangChiTiet')

const route = require('express').Router()

route.post('/donhang/:id', async function (req, res, next) {
    try {
        const existGH = await gioHang.findOne({ nguoi_dung_id: req.params.id, trang_thai: 1 });
        if (!existGH) {
            return res.status(404).json({ error: "Không tìm thấy giỏ hàng" });
        }

        const listCTSP = await gioHangChiTiet.find({ gio_hang_id: existGH._id });
        if (!listCTSP || listCTSP.length === 0) {
            return res.status(404).json({ error: "Không tìm thấy chi tiết giỏ hàng" });
        }

        const tongTien = listCTSP.reduce((accumulator, currentValue) => {
            return accumulator + (currentValue.gia * currentValue.so_luong)
        }, 0);

        const soLuong = listCTSP.reduce((accumulator, currentValue) => {
            return accumulator + currentValue.so_luong;
        }, 0);

        const objdh = new donHang({
            nguoi_dung_id: existGH.nguoi_dung_id,
            tong_gia: tongTien,
            so_luong: soLuong,
            trang_thai: "chờ xác nhận",
            phuong_thuc_thanh_toan: req.body.phuong_thuc_thanh_toan,
            dia_chi_giao_hang: req.body.dia_chi_giao_hang,
            ghi_chu: req.body.ghi_chu,
            tinh_trang: req.body.tinh_trang
        });

        const donHangSaved = await objdh.save();

        const listDHCT = listCTSP.map(async item => {
            const sp = await noiThat.findById(item.noi_that_id);
            if (!sp) {
                throw new Error(`Không tìm thấy sản phẩm với id ${item.noi_that_id}`);
            }
            let tongTienCT = item.so_luong * sp.gia;

            const dhct = new donHangChiTiets({
                don_hang_id: donHangSaved._id,
                // thêm
                noi_that_id: sp._id,
                tong_tien: tongTienCT,
                so_luong: item.so_luong
                // img: sp.hinh_anh,
                // mo_ta: sp.mo_ta
            });

            return await dhct.save();
        });

        await Promise.all(listDHCT);

        await gioHang.findByIdAndUpdate(existGH._id, { trang_thai: 0 });

        res.status(200).json("Success");
    } catch (err) {
        console.error(err);
        res.status(500).json("Lỗi máy chủ nội bộ");
    }
})

// Ấn nút để cập nhật trạng thái đơn hàng riêng cho admin và người dùng
route.put('/donhang/:id/trangthai', async function (req, res, next) {
    try {
        const hoadonId = req.params.id
        const newStrang_thai = req.body.trangthai
      
        const isAdmin = req.body.role
        const order = await donHang.findById(hoadonId)
        if (!order) {
            return res.status(404).json({ error: "Không tìm thấy đơn hàng" })
        }

        if (isAdmin) {
            order.trang_thai = newStrang_thai
        } else {
            if (order.trang_thai === "chờ xác nhận" && newStrang_thai === "hủy") {
                order.trang_thai = newStrang_thai
            } else {
                return res.status(403).json({ error: "Bạn không có quyền thay đổi trạng thái đơn hàng này" })
            }
        }
        const updateddonhang = await order.save()

        res.status(200).json(updateddonhang)
    } catch (err) {
        console.error(err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ" })
    }
})

//chờ, đã xác nhận /admin danh sách admin và nhân viên

//hủy /khách hàng
// list người dùng
// lấy danh sách cho người dùng, kiểm tra xem người dùng này có 
// if(role) thì hỏi xem có danh sách nào để hiện danh sách dh cho admin không //else thì hỏi xem có tồn tại dh của người dùng này không, nếu không thì hiện thông báo là không có
route.get('/donhang/:userId', async function (req, res, next) {
    try {
        const role = req.body.role
        if(role){
            const listHDtoAd = await donHang.find()
            if(!listHDtoAd || listHDtoAd.length == 0){
                return res.status(404).json({error : "Không tồn tại hóa đơn"})
            }
            console.log("Admin", listHDtoAd);

            return res.status(200).json(listHDtoAd)
        }else{
            const listHDtoUser = await donHang.find({nguoi_dung_id: req.params.userId})
            if(!listHDtoUser || listHDtoUser.length == 0){
                return res.status(404).json({error : "Không tồn tại hóa đơn"})
            }
            console.log("user", listHDtoUser);

            return res.status(200).json(listHDtoUser)
        }
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
    }
})

route.get("/donhangchitiet/:id", async function(req, res, next){
    try {
        const idDHCT = await donHangChiTiets.find({ don_hang_id: req.params.id });
        
        if(!idDHCT || idDHCT.length === 0){
            return res.status(404).json({ error: "Không tồn tại đơn hàng chi tiết" });
        }

        const listDHCT = [];
        for (const item of idDHCT) {
            const idnoithat = await noiThat.findById(item.noi_that_id);
            if(!idnoithat){
                console.log(`Không tìm thấy nội thất với id: ${item.noi_that_id}`);
                continue;
            }
            
            listDHCT.push({
                _id: item._id,
                don_hang_id: item.don_hang_id,
                ten_noi_that: idnoithat.ten_noi_that,
                mo_ta: idnoithat.mo_ta,
                img: idnoithat.hinh_anh,
                so_luong: item.so_luong,
                tong_tien: item.tong_tien
            });
        }

        res.status(200).json(listDHCT);
    } catch(err) {
        console.error(err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ" });
    }
});

//xuất ra hóa đơn khi tới
// lấy mọi thứ của đơn hàng để ra hóa đơn, hóa đơn chi tiết cũng lấy tương tự, mã hóa đơn là lấy từ zalo pay ý

module.exports = route

