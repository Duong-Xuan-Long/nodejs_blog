const Course=require('../models/Course');
const {multipleMongooseToObject}=require('../../util/mongoose');
class SiteController {
    index(req, res,next) {
        Course.find({})
        .then(courses=>{
            courses=multipleMongooseToObject(courses);
            res.render('home',{courses})})
        .catch(error=>next(error));
        // Course.find({},(err,courses,next)=>{
        //     if(!err) res.json(courses);
        //     else{
        //         next(err);
        //     }
        // })
        // res.render('home');
    }
    search(req, res) {
        res.render('search');
    }
}

module.exports = new SiteController();
