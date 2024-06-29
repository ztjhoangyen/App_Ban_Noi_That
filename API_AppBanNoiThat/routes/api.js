var express = require('express');
var router = express.Router();

const nguoiDungCtrl = require('../controller/nguoiDungCtrl')
const loaiNoiThatCtrl = require('../controller/loaiNoiThatCtrl')
const noiThatsCtrl = require('../controller/noiThatsCtrl')
const gioHang = require('../controller/gioHang')
const gioHangCT = require('../controller/gioHangCT')
const sapXep = require('../controller/sapXep')
const danhMucCtrl = require('../controller/danhMucCtrl')

router.use(nguoiDungCtrl)
router.use(loaiNoiThatCtrl)
router.use(noiThatsCtrl)
router.use(gioHang)
router.use(gioHangCT)
router.use(sapXep)
router.use(danhMucCtrl)


module.exports = router;
