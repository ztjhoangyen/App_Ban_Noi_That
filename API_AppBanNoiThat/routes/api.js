var express = require('express');
var router = express.Router();

const nguoiDungCtrl = require('../controller/nguoiDungCtrl')
const loaiNoiThatCtrl = require('../controller/loaiNoiThatCtrl')
const noiThatsCtrl = require('../controller/noiThatsCtrl')
const gioHang = require('../controller/gioHang')
const gioHangCT = require('../controller/gioHangCT')
const sapXep = require('../controller/sapXep')
const danhMucCtrl = require('../controller/danhMucCtrl')
const DonHangCtrl = require('../controller/DonHangCtrl')
const slideShowCtrl = require('../controller/slideShowCtrl')
const yeuThichCtrl = require('../controller/yeuThichCtrl')
const chatCtrl = require('../controller/chatCtrl')
const HoaDonCtrl = require('../controller/HoaDonCtrl')


router.use(nguoiDungCtrl)
router.use(loaiNoiThatCtrl)
router.use(noiThatsCtrl)
router.use(gioHang)
router.use(gioHangCT)
router.use(sapXep)
router.use(danhMucCtrl)
router.use(DonHangCtrl)
router.use(slideShowCtrl)
router.use(yeuThichCtrl)
router.use(chatCtrl)
router.use(HoaDonCtrl)


module.exports = router;
