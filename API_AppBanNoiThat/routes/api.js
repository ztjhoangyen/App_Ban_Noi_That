var express = require('express');
var router = express.Router();

const nguoiDungCtrl = require('../controller/nguoiDungCtrl')
const loaiNoiThatCtrl = require('../controller/loaiNoiThatCtrl')
const noiThatsCtrl = require('../controller/noiThatsCtrl')
const gioHang = require('../controller/gioHang')
const gioHangCT = require('../controller/gioHangCT')

router.use(nguoiDungCtrl)
router.use(loaiNoiThatCtrl)
router.use(noiThatsCtrl)
router.use(gioHang)
router.use(gioHangCT)


module.exports = router;
