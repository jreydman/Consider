### Architecture

---
root
 * package*.json
 * .yarn/[yarn]
 * yarn.lock
 * database/
   * [unit]/
     * BASEDATA.FDB
 * Docker/
   * db-[quoteName]-[backupdate(21072023)].Dockerfile
 * sh-scripts/
   * compose.sh
   * startup.sh
   * wait-for-it.sh
 * env-example
 * Makefile (deprecated)
---

### Dev preparations
```
cp env-example .env

yarn install
```

### use SH [compose]
   - in root dir
   ```
   /bin/bash ./sh-scripts/compose.sh <*/rostfarm/rostok>
   ```
   ### DB Auth
   [docker terminal]
   ```
   /usr/local/firebird/bin/isql -user <login> -password <password> localhost:/firebird/data/BASEDATA.FDB
   ```
   [bash shell]
   ```
   docker exec -it db-<quoteName>-<backupdate(21072023)> /usr/local/firebird/bin/isql -user <user> -password <password> localhost:/firebird/data/BASEDATA.FDB
   ```
### Use app scripts
   - /* primary pm - [yarn] */
   
   Dev construction realtime
   ```
   yarn dev
   ```
   Prod start
   ```
   yarn start
   ```
