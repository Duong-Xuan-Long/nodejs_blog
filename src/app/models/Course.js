const mongoose = require('mongoose');
var slug = require('mongoose-slug-generator');
var mongooseDelete = require('mongoose-delete');
const Schema = mongoose.Schema;

const Course = new Schema({
    name:{type:String,required:true, maxLength:255},
    description:{type:String, maxLength:600},
    image:{type:String, maxLength:255},
    videoId:{type:String,required:true , maxLength:255},
    level:{type:String, maxLength:255},
    // createdAt:{type:Date, default:Date.now},
    // updatedAt:{type:Date, default:Date.now},
    slug:{type:String,slug:"name",unique:true},
  },{
    timestamps:true,
  });
mongoose.plugin(slug);
Course.plugin(mongooseDelete,{ 
  deletedAt:true,
  overrideMethods: 'all' }),
  module.exports= mongoose.model('Course', Course);