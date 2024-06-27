const router = require('express').Router()
const nguoiDungs = require('../model/nguoiDung')
const bcrypt = require('bcryptjs');

router.post('/dangky', async function(req, res) {
    try {
        const { ten_tai_khoan, email, mat_khau, ho_ten, dia_chi, so_dien_thoai } = req.body;

        if (ten_tai_khoan.length < 10 || ten_tai_khoan.length > 15) {
            return res.status(422).json({ error: 'Tên tài khoản phải có từ 10 đến 15 ký tự.' });
        }

        if (mat_khau.length < 8 || mat_khau.length > 15) {
            return res.status(422).json({ error: 'Mật khẩu phải có từ 8 đến 15 ký tự.' });
        }

         // Kiểm tra tính duy nhất của email và tên tài khoản
         const existingUser = await nguoiDungs.findOne({ $or: [{ email }, { ten_tai_khoan }] });
         if (existingUser) {
             return res.status(409).json({ error: 'Email hoặc tên tài khoản đã tồn tại.' });
         }

         const newUser = new nguoiDungs({
            ten_tai_khoan,
            email,
            mat_khau,
            ho_ten
            // CHÝ Ý: tạo sau
            // dia_chi,
            // so_dien_thoai
        });

        await newUser.save();
        res.status(201).json({ message: 'Người dùng đã được tạo thành công.', user: newUser });

    } catch (err) {
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." });
    }
});

router.post('/dangnhap', async function(req, res, next){
    const { ten_tai_khoan, mat_khau } = req.body;
    try{
        const user = await nguoiDungs.findOne({ ten_tai_khoan });

        if (!user) {
            return res.status(404).json({ error: 'Tên tài khoản không tồn tại.' });
        }
// Đang bị vấn đê mật khẩu không chính xác
        const isMatch = await bcrypt.compare(mat_khau, user.mat_khau);

        if (!isMatch) {
            return res.status(400).json({ error: 'Mật khẩu không chính xác.' });
        }

        res.status(200).json({ message: 'Đăng nhập thành công.', user });
    }catch(err){
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." });
    }
})

module.exports = router