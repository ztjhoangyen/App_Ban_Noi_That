const router = require('express').Router()
const nguoiDungs = require('../model/nguoiDung')

router.post('/dangky', async function(req, res) {
    try {
        const { ten_tai_khoan, email, mat_khau, ho_ten, dia_chi, so_dien_thoai, role } = req.body

        if (ten_tai_khoan.length < 10 || ten_tai_khoan.length > 15) {
            return res.status(422).json({ error: 'Tên tài khoản phải có từ 10 đến 15 ký tự.' })
        }

        if (mat_khau.length < 8 || mat_khau.length > 15) {
            return res.status(422).json({ error: 'Mật khẩu phải có từ 8 đến 15 ký tự.' })
        }

         // Kiểm tra tính duy nhất của email và tên tài khoản
         const existingUser = await nguoiDungs.findOne({ $or: [{ email }, { ten_tai_khoan }] })
         if (existingUser) {
             return res.status(409).json({ error: 'Email hoặc tên tài khoản đã tồn tại.' })
         }

         const newUser = new nguoiDungs({
            ten_tai_khoan,
            email,
            mat_khau,
            ho_ten,
            role,
            socketId
            // CHÝ Ý: tạo sau
            // dia_chi,
            // so_dien_thoai
        })

        await newUser.save()
        res.status(201).json(newUser)

    } catch (err) {
        console.error("Error:", err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})

router.post('/dangnhap', async (req, res) => {
    const { ten_tai_khoan, mat_khau } = req.body
    try {
        const user = await nguoiDungs.findOne({ ten_tai_khoan })

        if (!user) {
            return res.status(404).json({ error: 'Tên tài khoản không tồn tại.' })
        }

        if (mat_khau !== user.mat_khau) {
            return res.status(400).json({ error: 'Mật khẩu không chính xác.' })
        }

        res.status(200).json(user)
    } catch (err) {
        console.error("Error:", err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})

router.post("/UpdateSocketId", async (req, res) =>{
    const {id, socket} = req.body
    try {
        let result = await nguoiDungs.findOneAndUpdate(
        //    id nguoi dung
            {_id : id},
            {
                $set:{
                    socketId: socket 
                },
            },
            {new: true}
        )
        res.status(200).json(result)
    } catch (error) {
        res.status(500).json(error)
    }
})

router.get('/nguoidung', async (req, res) => {
    try {
        const { id } = req.query;

        const ngdungs = await nguoiDungs.find()

        if (!ngdungs) {
            return res.status(404).json({ error: 'Không tồn tại dữ liệu người dùng.' })
        }
        let result = ngdungs.filter((item) => item._id != id);

        res.status(200).json(result)
    } catch (err) {
        console.error("Error:", err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})


module.exports = router