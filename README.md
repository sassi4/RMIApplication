# RMIApplication
It's about RMI sample with distributed system.

# The commands below present the basic commands for executing this project.
 ### 1. ant clean »  then « ant » to build.
 ### 2.	cd bin
 ### 3.	rmiregistry &
 ### 4.	cd ..
 ### 5.	./nameserver.sh ». The nameServer will display its IP if everything is correct.
 ### 6.	Start compute servers .Pour ce faire. Example : 
      #### a.	./server.sh -m 0 -q 4 -p 5002 IP_nameServer 
      #### b.	With : 
      #####    i.	-m le taux de malice en %
      #####    ii.	-q la capacité 
      #####    iii.	-p le port
  ### 7.	To start the load balancer :
        ##### a.	 ./client.sh <operationfilename> (-u ou -s)
            ##### i.	In the absence of the "u" or "s" parameters, the secure mode is selected by default.
