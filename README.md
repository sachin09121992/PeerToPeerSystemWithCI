### Peer-To-Peer System with Centralized Index

## Team Information

1) Sachin Ahuja (sahuja3)
2) Sameera Lanka (slanka)

## Project Summary:

We created a peer to peer system with centralized index. The core of the system lies in the facts that actual required data(in this case RFC) is transferred between the peers instead of server providing the actual document. The brief description about the server and client is shown below:

Server: The server maintains the list of all the nodes that are up and it also maintains a list of RFC's that are available to the system. It runs a thread that accepts a client request and respond back with proper responses or errors in case of invalid requests.

Client: The clients are actually the peers. The client communicates with server when they come up and server add its to the list. It also adds entries for all the RFCs that this client initially has. Each client runs two threads one for communication with server and the other for listening to the requests made by other peers. Each client is allocated a random port number.A folder on the client machine is choosen as the repository for all the RFCs that it contains which is taken as an input. It will then get several options, user can choose to either ADD, LOOKUP or get a LIST of all available RFC's with the server. It can also request another active peer for RFC and download it from there. After downloading an RFC, it will inform server about this addition and it will be entered in the list maintained by the server.

## Instructions to compile and run code

(1) Download Eclipse IDE with Java perspective.
(2) Unzip the code submitted by us.
(3) Open eclipse and create a new project by selecting our folder(the unzipped copy of our folder).
(4) Run Server.java in the server package.
(5) Run Client.java in the client package.
(6) Enter the folder which you want to give as a repository.
(7) Enter the IP address of the machine on which you started the server process by executing the Server.java process in step 4
(8) You can now see a menu, please select an option and enjoy :-)


