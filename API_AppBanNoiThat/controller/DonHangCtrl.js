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
            console.log("Không tìm thấy giỏ hàng" + existGH);
            return res.status(404).json({ error: "Không tìm thấy giỏ hàng" });
        }

        const listCTSP = await gioHangChiTiet.find({ gio_hang_id: existGH._id });
        if (!listCTSP || listCTSP.length === 0) {
            console.log("Không tìm thấy chi tiết giỏ hàng");

            return res.status(404).json({ error: "Không tìm thấy chi tiết giỏ hàng" });
        }

        // biến kt số lượng tồn kho
        let quaSL = false
        for(const item of listCTSP){
            const sp = await noiThat.findById(item.noi_that_id)
            if(!sp){
                console.log("Không tìm thấy sản phẩm với id ");

                return res.status(404).json({ error: `Không tìm thấy sản phẩm với id ${item.noi_that_id}` })
            }
            // cập nhật lại sl nội thất tồn tại trong giỏ hàng
            if (item.so_luong > sp.so_luong) {
                quaSL = true;
                await gioHangChiTiet.findByIdAndUpdate(item._id, { so_luong: sp.so_luong });
            }
        }

        // sp vượt quá sl nội thất thì yêu cầu vào coi lại giỏ hàng
        if (quaSL) {
            console.log("Một số sản phẩm vượt quá số lượng tồn kho. Mình");

            return res.status(400).json({ error: "Một số sản phẩm vượt quá số lượng tồn kho. Mình đã cập nhật lại số lượng trong giỏ hàng. Vui lòng kiểm tra lại giỏ hàng của bạn." });
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
            trang_thai: "Đang xử lý",
            phuong_thuc_thanh_toan: req.body.phuong_thuc_thanh_toan,
            dia_chi_giao_hang: req.body.dia_chi_giao_hang,
            ghi_chu: req.body.ghi_chu,
            tinh_trang: req.body.tinh_trang
        });

        const donHangSaved = await objdh.save()

        const listDHCT = listCTSP.map(async item => {
            const sp = await noiThat.findById(item.noi_that_id);
            if (!sp) {
                console.log("KKhông tìm thấy sản phẩm với id ");

                throw new Error(`Không tìm thấy sản phẩm với id ${item.noi_that_id}`);
            }
            let tongTienCT = item.so_luong * sp.gia;

            const dhct = new donHangChiTiets({
                don_hang_id: donHangSaved._id,
                noi_that_id: sp._id,
                tong_tien: tongTienCT,
                so_luong: item.so_luong
            })

            return await dhct.save();
        })

        await Promise.all(listDHCT);

        await gioHang.findByIdAndUpdate(existGH._id, { trang_thai: 0 });

        res.status(200).json("Success");
    } catch (err) {
        console.error(err)
        res.status(500).json("Lỗi máy chủ nội bộ");
    }
})

// Cập nhật trạng thái xong thì thay đổi tên người dùng
// Ấn nút để cập nhật trạng thái đơn hàng riêng cho admin và người dùng
// chuyển sang xác nhận thì trừ đi số lượng
route.put('/donhang/:id/trangthai', async function (req, res, next) {
    try {
        const hoadonId = req.params.id
        const newStrang_thai = req.body.trangthai
        const isAdmin = req.body.role
        const order = await donHang.findById(hoadonId)
      
        console.log("isAdmin: " + isAdmin + " | newStrang_thai: " + newStrang_thai);

        if (!order) {
            return res.status(404).json({ error: "Không tìm thấy đơn hàng" })
        }

        if (isAdmin) {
            if(order.trang_thai === "Đang xử lý" && newStrang_thai === "Đã xác nhận"){
            //  trạng thái đã xác nhận thì trừ đi số lượng
                 const HdctBYHd = await donHangChiTiets.find({don_hang_id: order._id})

                for(let i = 0; i < HdctBYHd.length; i++){
                    let nthat = await noiThat.findById(HdctBYHd[i].noi_that_id)
            
                    if (nthat) {
                        nthat.so_luong = nthat.so_luong - HdctBYHd[i].so_luong
                        await nthat.save()
                    } else {
                        return res.status(404).json({ error: "Không tìm thấy nội thất trong đơn hàng chi tiết" })
                    }

                }

                order.trang_thai = newStrang_thai
                console.log("newStrang_thai" + newStrang_thai);
          
            }else if(order.trang_thai === "Đang xử lý" && newStrang_thai === "Đã hủy"){
                order.trang_thai = newStrang_thai
            // thêm
            }else if(order.trang_thai === "Đã xác nhận" && newStrang_thai === "Đã xuất hóa đơn"){
                order.trang_thai = newStrang_thai
            }
        } else {
            if (order.trang_thai === "Đang xử lý" && newStrang_thai === "Đã hủy") {
                order.trang_thai = newStrang_thai
                console.log("newStrang_thai" + newStrang_thai);
            } else {
                console.log("No" + newStrang_thai + order.trang_thai);
                return res.status(403).json({ error: "Bạn không có quyền thay đổi trạng thái đơn hàng này" })
            }
        }
        const updateddonhang = await order.save()
        console.log("updateddonhang" + updateddonhang);

        res.status(200).json(updateddonhang)
    } catch (err) {
        console.error(err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ" })
    }
})

route.get('/donhang/:userId', async function (req, res, next) {
    try {
        const role = req.query.role === 'true'

        if(role){
            const listHDtoAd = await donHang.find()
            if(!listHDtoAd || listHDtoAd.length == 0){
                return res.status(404).json({error : "Không tồn tại đơn hàng"})
            }
            console.log("Admin", listHDtoAd)

            return res.status(200).json(listHDtoAd)
        }else{
            const listHDtoUser = await donHang.find({nguoi_dung_id: req.params.userId})
            if(!listHDtoUser || listHDtoUser.length == 0){
                return res.status(404).json({error : "Không tồn tại hóa đơn"})
            }
            console.log("user", listHDtoUser)

            return res.status(200).json(listHDtoUser)
        }
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ" })
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
})

route.get("/thongtindonhang/:id", async function(req, res, next){
    try {
        const idDH = await donHang.findById(req.params.id)
        
        if(!idDH){
            res.status(404).json({error: "Không tồn tại thông tin đơn hàng"})
        }
            res.status(200).json(idDH)
    } catch(err) {
        console.error(err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ" })
    }
})

module.exports = route