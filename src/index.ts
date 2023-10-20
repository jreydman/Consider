import 'dotenv/config'
import { executeQuery } from "./libs/database.js"
import * as console from "console";
import firebird from "node-firebird";
import { Cennik, ICennik } from "./types/cennik.js";
console.log(`App: started!`);

(function() {
  
  const prompt: string = `select * from cennik`
  const filter: any = []
  const callback: firebird.QueryCallback = (err, result) => {
    if(err) throw err
    console.log("native: "+result[0]["DBEFOR"])
    const test = new Cennik(result[0])
    console.log("class: "+test.DBEFOR.getDate())
    console.log("interface: "+(result[0]["DBEFOR"] as ICennik))
  }
  
  executeQuery(prompt, filter, callback)
  
})()