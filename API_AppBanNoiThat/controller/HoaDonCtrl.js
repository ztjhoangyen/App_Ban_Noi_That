const hoaDons = require('../model/hoaDon')
const hoaDonChiTiets = require('../model/hoaDonChiTiet')
const donHangChiTiets = require('../model/donHangChiTiet')

const route = require('express').Router()

route.post("/hdAndHdct", async function (req, res, next) {
    try {
        const hoadon = new hoaDons({
            don_hang_id: req.body.don_hang_id
        })

        let hd = await hoadon.save()

        const dhcts = await donHangChiTiets.find({ don_hang_id: req.body.don_hang_id })

        for (let i = 0; i < dhcts.length; i++) {
            let hdct = new hoaDonChiTiets({
                don_hang_ct_id: dhcts[i]._id,
                hoa_don_id: hd._id
            })
            await hdct.save()
        }

        res.status(200).json({ message: "Success" })

    } catch (err) {
        console.error("Error:", err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})


route.get("/hoadon", async function (req, res, next) {
    try {
        const allhoadon = await hoaDons.find().populate('don_hang_id')

        if (allhoadon.length == 0) {
            return res.status(404).json({ error: "Not found" })
        }

        res.status(200).json(allhoadon)
    } catch (err) {
        console.error("Error:", err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})

route.get("/itemhoadon/:id", async function (req, res, next) {
    try {
        const itemhoadon = await hoaDons.findById(req.params.id).populate('don_hang_id')

        if (!itemhoadon) {
            return res.status(404).json({ error: "Not found" })
        }

        res.status(200).json(itemhoadon)
    } catch (err) {
        console.error("Error:", err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})

route.get("/hoadonchitiet/:id", async function (req, res, next) {
    try {
        const allhoadonct = await hoaDonChiTiets.find({hoa_don_id: req.params.id}).populate('don_hang_ct_id')

        if (allhoadonct.length == 0) {
            return res.status(404).json({ error: "Not found" })
        }

        res.status(200).json(allhoadonct)
    } catch (err) {
        console.error("Error:", err)
        res.status(500).json({ error: "Lỗi máy chủ nội bộ." })
    }
})

module.exports = route