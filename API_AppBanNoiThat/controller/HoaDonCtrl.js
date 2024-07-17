const hoaDons = require('../model/hoaDon')

const route = require('express').Router()
route.post("/hoadon", async function(req, res, next){
    try{
        // Làm slide show trong chat và file rồi mới làm bên android r mới tới hóa đơn
        // đang làm hóa đơn, suy nghĩ nếu đã thanh toán zalo pay rồi thì tính sao
    }catch(err){
        console.error("Error:", err);
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." });
    }
})
// xuất hóa đơn thì hỏi xem còn 
module.exports = route