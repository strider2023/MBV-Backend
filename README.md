MBV-Backend deployment steps
============================

* Once your have completed ur code changes goto the appropriate module and run this command to generate the deployment zip 

../gradlew distZip

* Copy the above generated zip to the cloud for deployment using the below command

scp -i ~/<permission>.pem ./build/distributions/api.zip ec2-user@boxIP: 

* Login to the box 

ssh -i ~/Documents/Work/pem/C1X-Stag-New.pem  ec2-user@boxIP

* Find and kill the process (api or jobs)

ps -ef | grep java

or

ps -auxw

kill <processid>

* Extract the updated zip 

unzip api.zip

* Go to folder

cd api/bin

* Run api

nohup ./api &1

screen

[control + D]

exit

------------------------

NOTE: Mail me for properties file.


####Install Java 1.8 in the BOX####
===================================

sudo yum remove java-1.7.0-openjdk

sudo yum install java-1.8.0


####Install Memcached in the BOX####
====================================

sudo yum install memcached

