const mongoose = require('mongoose');
var slug = require('mongoose-slug-generator');
var mongooseDelete = require('mongoose-delete');
const Schema = mongoose.Schema;
const AutoIncrement = require('mongoose-sequence')(mongoose);

const CourseSchema = new Schema({
  _id:{type:Number},
    name:{type:String,required:true, maxLength:255},
    description:{type:String, maxLength:600},
    image:{type:String, maxLength:255},
    videoId:{type:String,required:true , maxLength:255},
    level:{type:String, maxLength:255},
    // createdAt:{type:Date, default:Date.now},
    // updatedAt:{type:Date, default:Date.now},
    slug:{type:String,slug:"name",unique:true},
  },{
    _id:false,
    timestamps:true,
  });
  //custom querry helpers
  CourseSchema.query.sortable=function(req){
    if(req.query.hasOwnProperty('_sort')){
      const isValidType=['asc','desc'].includes(req.query.type);
      return this.sort({
      [req.query.column]:isValidType?req.query.type:'desc',
      });
      
  }
  return this;
  }
mongoose.plugin(slug);
CourseSchema.plugin(AutoIncrement);
CourseSchema.plugin(mongooseDelete,{ 
  deletedAt:true,
  overrideMethods: 'all' }),
  module.exports= mongoose.model('Course', CourseSchema);