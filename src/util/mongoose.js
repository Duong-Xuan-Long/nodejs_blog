module.exports={
    multipleMongooseToObject:function(mongoose){
        return mongoose.map(m=>m.toObject());
    },
    mongooseToObject:function(mongoose){
        return mongoose.toObject(); 
    }
}