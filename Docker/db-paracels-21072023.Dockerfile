FROM jacobalberty/firebird:2.5-ss
MAINTAINER Pikj [Jreydman] Reyderman <pikj.reyderman@gmail.com>

# path to database
COPY ./database/Paracels/BASEDATA.FDB /firebird/data/
