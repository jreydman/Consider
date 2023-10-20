import 'dotenv/config'
import firebirdClient from "./libs/firebirdClient.js"

console.log("START APP");

firebirdClient.attach((err,db)=>{
  if(err) throw err
  
  
})