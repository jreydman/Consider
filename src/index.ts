import 'dotenv/config'
import { executeQuery } from "./libs/database.js"
import * as console from "console";
import firebird from "node-firebird";
import { Cennik, ICennik } from "./types/cennik.js";
import ExcelJS from "exceljs"
import * as path from "path";
import * as fs from "fs";
import * as process from "process";
import { fileURLToPath } from 'url';
console.log(`App:\t\tstarted!`);

(async function() {

  // QUERY
  let dataPool: ICennik[] = []
  const prompt: string = `select * from cennik`
  const filter: any = []
  
  
  const excelDir = path.dirname(path.dirname(fileURLToPath(import.meta.url)))
  const workbook = new ExcelJS.Workbook()
  const sheetCennik = workbook.addWorksheet('Cennik');
  workbook.views = [
    {
      x: 0, y: 0, width: 10000, height: 20000,
      firstSheet: 0, activeTab: 1, visibility: 'visible'
    }
  ]
  
  const callback: firebird.QueryCallback = async (err, result) => {
    if(err) throw err
    dataPool = dataPool.concat(result.map(obj=>obj))
    console.log(`Excel:\t\tpush!`);
    sheetCennik.addRow(dataPool[0])

    
    const filePath = path.join(excelDir, 'example.xlsx')
    console.log(`App:\t\tcreating file!`)
    fs.open(filePath, 'w', (err) => {
      if(err) throw err;
      console.log(`App:\t\tfile created!\t\t${filePath}`);

      console.log(`Excel:\t\tstart persist file!\t${filePath}`);
      workbook.xlsx.writeFile(filePath)
      console.log(`Excel: file saved!`)
      
    });
  }
  executeQuery(prompt, filter, callback)
})()