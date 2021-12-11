How to run:
1. Compile all packages from the project root
javac client/*.java, javac common/*.java, javac server/*.java
2. Run the server from the project root
java server/BattleShipDriver <port>
3. Run as many clients as needed from the project root
java client/BattleDriver <dest> <port>

Change DEFAULT_SIZE in BattleServer.java to change the square size.