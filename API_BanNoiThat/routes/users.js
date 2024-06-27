var express = require('express');
var router = express.Router();

const nguoiDungCtrl = require('../controller/nguoiDungCtrl')

router.use(nguoiDungCtrl)

module.exports = router;
