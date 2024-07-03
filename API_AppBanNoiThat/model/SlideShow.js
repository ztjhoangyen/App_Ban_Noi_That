const mongoose = require('mongoose')

const SlideShowsSchema = new mongoose.Schema({
    url: {
        type: String
    }
})
module.exports = new mongoose.model('slideShows', SlideShowsSchema)