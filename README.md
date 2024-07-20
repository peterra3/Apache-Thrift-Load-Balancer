
# Apache-Thrift-Load-Balancer


## Overview
This project involves building a distributed system in Java for computing the bcrypt key derivation function using Apache Thrift 0.13.0 and Java 11. The system includes a client layer, a front-end (FE) layer, and a back-end (BE) layer, designed to handle password hashing and checking operations with load balancing and scalability.

## Objectives
1. Gain hands-on experience with Apache Thrift.
2. Learn about scalability and load balancing.

## Software Architecture
The system architecture consists of:
- **Clients**: Communicate exclusively with the FE node.
- **FE Node**: Relays requests between clients and BE nodes, balancing the load.
- **BE Nodes**: Handle the actual computation of bcrypt functions.

## Functional Requirements
The system supports two primary operations:
1. **Hash Password**: Hash a given password with a specified number of bcrypt rounds. The output includes the number of rounds, the cryptographic salt, and the hash.
2. **Check Password**: Verify that a given password matches a previously computed hash.

## RPC Interface for FE Layer
The FE layer uses the following Thrift service interface (defined in the starter code):

```thrift
exception IllegalArgument {
  1: string message;
}

service BcryptService {
  list<string> hashPassword(1: list<string> password, 2: i16 logRounds) throws (1: IllegalArgument e);
  list<bool> checkPassword(1: list<string> password, 2: list<string> hash) throws (1: IllegalArgument e);
}
```

### Exceptions
The FE node throws an `IllegalArgument` exception in the following cases:
- The password and hash arguments of `checkPassword` are lists of unequal length.
- The `logRounds` argument of `hashPassword` is out of range for jBcrypt.
- Malformed hash passed to `checkPassword`.

No exceptions are thrown for:
- No BE nodes available to perform computations.
- Password length is zero (i.e., password = empty string).

## Getting Started

### Prerequisites
- Java 11
- Apache Thrift 0.13.0
- Guava (included in the starter code)

### Setup and Build
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/bcrypt-distributed-system.git
   cd bcrypt-distributed-system
   ```

2. Compile the Thrift IDL:
   ```bash
   thrift --gen java bcrypt_service.thrift
   ```

3. Build the project:
   ```bash
   ./gradlew build
   ```

### Running the System
1. Start the BE nodes:
   ```bash
   java -jar build/libs/bcrypt-backend.jar
   ```

2. Start the FE node:
   ```bash
   java -jar build/libs/bcrypt-frontend.jar
   ```

3. Run the client:
   ```bash
   java -jar build/libs/bcrypt-client.jar
   ```

## License
This project is licensed under the MIT License.

## Contact
For questions or issues, please contact me via GitHub.

