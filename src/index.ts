import 'dotenv/config'
import firebirdClient from "./libs/firebirdClient.js"

console.log(`App: started!`);

(()=>{

  firebirdClient.attach(()=>{
    firebirdClient.driver.query("",[],()=>{})
  })
  
})()